package com.polar.industries.teskotlin.helpers

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.io.UnsupportedEncodingException
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class SenderAsyncTask(
    session: Session,
    from: String,
    to: String,
    context: Context,
    datos: Array<String>
) :
    AsyncTask<String?, String?, String?>() {
    private val from: String
    private val to: String
    private var progressDialog: ProgressDialog? = null
    private val session: Session
    private val context: Context
    private val datos: Array<String>

    init {
        this.session = session
        this.from = from
        this.to = to
        this.context = context
        this.datos = datos
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog.show(context, "Enviando", "Espere", true)
        progressDialog!!.setCancelable(false)
    }

    protected override fun doInBackground(vararg params: String?): String? {
        try {
            if (datos.size == 2) {
                recuperacionCredenciales()
            }
        } catch (e: MessagingException) {
            e.printStackTrace()
            return e.message
        } catch (e: Exception) {
            e.printStackTrace()
            return e.message
        }
        return null
    }

    protected override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        progressDialog!!.setMessage(values[0])
    }

    override fun onPostExecute(result: String?) {
        progressDialog!!.dismiss()
        if (datos.size == 2) {
            Toast.makeText(
                context,
                "Se enviará un correo al email encontrado en el sistema con las credenciales",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Throws(UnsupportedEncodingException::class, MessagingException::class)
    private fun recuperacionCredenciales() {
        val mimeMessage: Message = MimeMessage(session)
        mimeMessage.setFrom(InternetAddress(from, "Talachitas Express Service"))
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
        mimeMessage.setSubject("Recuperación de credenciales")
        val htmlText2 =
            "<p ALIGN=\"center\"><img  width=\"90\" height=\"210\" src=\"https://firebasestorage.googleapis.com/v0/b/app-tes-ba6e9.appspot.com/o/LogoTES.jpg?alt=media&token=c35a0d4f-d7ee-47cc-b3af-32b73d166a82\"></p>"
        val htmlText = ("<body> " +
                "<h4><font size=3 face=\"Sans Serif,arial,verdana\">Hola, " + datos[0] + "</font></h4> " +
                "<br>" +
                htmlText2 +
                "<hr>" +
                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "Tus credenciales <strong>" + datos[0] /*nombre*/
                +
                "</strong> son:" + "</font></p>" +
                "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "<br><strong>" + to + "<br><br>" + datos[1] /*contraseña*/ + "</strong>" + "</font></p>" +
                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Si tú no solisitaste esto no tienes porque preocuparte tus datos están protegidos.</p>" +
                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>" +
                "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#EA2925\" size=3 face=\"Sans Serif,arial,verdana\">Talachitas Express Service (TES).</font>.</p>" +
                "<br>" +
                "<hr>" +
                "<footer>" +
                "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>" +
                "</footer>" +
                "</body>")
        mimeMessage.setContent(htmlText, "text/html; charset=utf-8")
        Transport.send(mimeMessage)
    }
}