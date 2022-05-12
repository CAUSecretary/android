package com.example.causecretary.ui.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GmailSender: Authenticator() {
    //보내는 사람 이메일과 비밀번호
    val fromEmail = "kimdooooreeee@gmail.com"
    val password = "minjilove1206^^"

    // 메일 보내기
    fun sendEmail(toEmail: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val props = Properties()
            props.setProperty("mail.transport.protocol", "smtp")
            props.setProperty("mail.host", "smtp.gmail.com")
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.port"] = "465"
            props["mail.smtp.socketFactory.port"] = "465"
            props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
            props["mail.smtp.socketFactory.fallback"] = "false"
            props.setProperty("mail.smtp.quitwait", "false")

            // 구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달
            val session = Session.getDefaultInstance(props, this@GmailSender)

            // 메시지 객체 만들기
            val message = MimeMessage(session)
            message.sender = InternetAddress(fromEmail)                                 // 보내는 사람 설정
            message.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))    // 받는 사람 설정
            message.subject = "이메일 제목"                                              // 이메일 제목
            message.setText("이메일 내용")                                               // 이메일 내용

            // 전송
            Transport.send(message)
        }
    }
}