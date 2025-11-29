package br.com.chase.utils

import android.util.Log
import br.com.chase.data.model.RouteRequest
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class MalignoServerUtils(
    private val host: String = "192.168.15.6",
    private val port: Int = 3000,
    private val callback: (Boolean) -> Unit
) : Thread() {
    private var pedido: RouteRequest? = null

    fun enviarPedido(routeRequest: RouteRequest) {
        pedido = routeRequest
        Log.d("RotaValidatorClient", "📤 Pedido preparado para envio: $routeRequest")
        start()
    }

    override fun run() {
        try {
            val req = pedido!!

            Log.d("RotaValidatorClient", "🔌 Conectando em $host:$port ...")

            Socket(host, port).use { socket ->
                Log.d("RotaValidatorClient", "✅ Conectado ao servidor!")

                val out = DataOutputStream(socket.getOutputStream())
                val input = DataInputStream(socket.getInputStream())

                out.writeUTF(req.uid)
                out.writeUTF(req.name)
                out.writeUTF(req.description)
                out.writeUTF(req.startLocation)
                out.writeUTF(req.endLocation)
                out.writeUTF(req.distance.toString())
                out.writeUTF(req.recordTime)

                out.writeInt(req.points.size)
                for (point in req.points) {
                    out.writeDouble(point.latitude)
                    out.writeDouble(point.longitude)
                }

                out.flush()

                Log.d("RotaValidatorClient", "📤 Envio concluído. Aguardando resposta...")

                val resposta = input.readUTF()
                Log.d("RotaValidatorClient", "📥 Resposta recebida: $resposta")

                callback(resposta == "true")
            }

        } catch (e: Exception) {
            Log.e("RotaValidatorClient", "❌ Erro na comunicação com o servidor:", e)
            callback(false)
        }
    }
}