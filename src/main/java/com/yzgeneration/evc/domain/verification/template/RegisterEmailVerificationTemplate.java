package com.yzgeneration.evc.domain.verification.template;

public class RegisterEmailVerificationTemplate extends EmailVerificationTemplate{

    public RegisterEmailVerificationTemplate(String verificationCode) {
        super("의가교환 회원가입 인증 메일입니다.");
        generateContent(verificationCode);
    }

    @Override
    protected void generateContent(String verificationCode) {
        this.content = """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: 'Helvetica Neue', Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f8f9fc;
                        color: #333333;
                    }
                    .container {
                        max-width: 600px;
                        margin: 40px auto;
                        background: #ffffff;
                        border-radius: 12px;
                        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                        border: 1px solid #eaeaea;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 24px;
                    }
                    .content {
                        padding: 30px 20px;
                    }
                    .content h2 {
                        color: #333333;
                        font-size: 20px;
                        margin-bottom: 10px;
                    }
                    .content p {
                        font-size: 16px;
                        line-height: 1.6;
                        color: #555555;
                        margin-bottom: 20px;
                    }
                    .button {
                        display: inline-block;
                        background-color: #4CAF50;
                        color: #ffffff;
                        text-decoration: none;
                        padding: 12px 25px;
                        font-size: 16px;
                        border-radius: 8px;
                        text-align: center;
                        margin-top: 20px;
                        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
                        transition: background-color 0.3s, box-shadow 0.3s;
                    }
                    .button:hover {
                        background-color: #45a049;
                        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
                    }
                    .footer {
                        font-size: 14px;
                        color: #888888;
                        text-align: center;
                        padding: 20px;
                        background-color: #f8f9fc;
                        border-top: 1px solid #eaeaea;
                    }
                    .footer {
                        color: #4CAF50;
                        text-decoration: none;
                    }
                    .footer a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>의가교환</h1>
                    </div>
                    <div class="content">
                        <h2>메일인증 안내입니다.</h2>
                        <p>안녕하세요.<br>
                        아래 '메일 인증' 버튼을 클릭하여 회원가입을 완료해 주세요.<br>
                        감사합니다.</p>
                    <a href="http://ec2-15-164-152-88.ap-northeast-2.compute.amazonaws.com:8080/api/members/verify?code=%s" class="button">메일 인증</a>
                    </div>
                   
                </div>
            </body>
                </html>
            """.formatted(verificationCode);
    }
}
