package cn.navyd.app.supermarket.user.reset;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.navyd.app.supermarket.base.ServiceException;
import cn.navyd.app.supermarket.config.EmailProperties;
import cn.navyd.app.supermarket.user.User;
import cn.navyd.app.supermarket.user.securecode.AbstractEmailSecureCodeService;
import cn.navyd.app.supermarket.user.securecode.SecureCodeGenerator;

@Service
public class EmailForgotPasswordServiceImpl extends AbstractEmailSecureCodeService implements EmailForgotPasswordService {
  @Autowired
  private EmailProperties sender;
  @Autowired
  private SecureCodeGenerator codeGenerator;
  
  @Override
  protected String doSendCode(User user) {
    final String toUsername = user.getUsername(), toAddress = user.getEmail();
    final String code = codeGenerator.next();
    Email email = EmailBuilder.startingBlank()
        .from(sender.getUsername(), sender.getAddress())
        .to(toUsername, toAddress)
        .withSubject("app用户找回密码邮件")
        .withPlainText("尊敬的用户：" + toUsername + "，你好！\n" 
            + "您的重置密码的激活码：\n" + code + "\n为保障您的帐号安全，请在" 
            + getDuration().toMinutes() + "分钟内使用该代码重置密码。"
            + "如果您并未尝试重置密码，请忽略本邮件，由此给您带来的不便请谅解。")
        .buildEmail();
    Mailer mailer = MailerBuilder.withSMTPServer(
        sender.getHost(), 
        sender.getPort(), 
        sender.getAddress(), 
        sender.getPassword())
      .withTransportStrategy(TransportStrategy.SMTP_TLS)
      .withDebugLogging(sender.getDebug())
      .withSessionTimeout(sender.getSessionTimeout())
      .buildMailer();
    try {
      mailer.sendMail(email);
      return code;
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }
  }
}
