
	$(document).ready(function() {
	function validateForm() {
	    // 아이디 중복 확인 체크 마크가 표시되어 있는지 확인
	    var isIdValid = $('#checkmark').is(':visible');
	    if (!isIdValid) {
	        console.log("아이디가 유효하지 않습니다.");
	        return false;
	    }

	    // 비밀번호 유효성 검사 체크 마크가 표시되어 있는지 확인
	    var isPwdValid = $('#pwdCheckmark').is(':visible');
	    if (!isPwdValid) {
	        console.log("비밀번호가 유효하지 않습니다.");
	        return false;
	    }

	    // 이메일 인증 체크 마크가 표시되어 있는지 확인
	    var isEmailVer = $('#veriCheckmark').is(':visible');
	    if (!isEmailVer) {
	        console.log("이메일이 인증되지 않았습니다.");
	        return false;
	    }

	    // 모든 필드가 유효한 경우
	    return true;
	}
  
    $('#signupForm').submit(function(event) {
        if (!validateForm()) {
            alert("모든 필드를 올바르게 작성해주세요.");
            event.preventDefault();
        }
    });

    $('#sendCode').click(function() {
        var email = $('#user_email').val();
        console.log("Sending verification code to: " + email);
        $.ajax({
            url: '/sendCode',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }),
            success: function(response) {
                console.log("Response: ", response);
                if (response.status === "success") {
                    alert("인증번호가 이메일로 전송되었습니다.");
                    $('#veriCodeContainer').show();
                } else {
                    alert("인증번호 전송에 실패했습니다.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error: ", textStatus, errorThrown);
                alert("인증번호 전송에 실패했습니다.");
            }
        });
    });

    $('#verifyCode').click(function() {
        var email = $('#user_email').val();
        var code = $('#veri_code').val();
        console.log("Verifying code for: " + email);
        $.ajax({
            url: '/verifyCode',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({ email: email, code: code }),
            success: function(response) {
                console.log("Response: ", response);
                if (response.status === "success") {
                    $('#veriCheckmark').show();
                    alert(response.message);
                } else {
                    $('#veriCheckmark').hide();
                    alert(response.message);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error: ", textStatus, errorThrown);
                alert("인증번호 확인에 실패했습니다.");
            }
        });
    });

    $('#checkIdBtn').click(function() {
        var userId = $('#user_id').val();
        console.log("Checking ID for: " + userId);
        $.ajax({
            url: '/checkUserId',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify({ userId: userId }),
            success: function(response) {
                console.log("Response: ", response);
                if (response.status === "available") {
                    $('#checkmark').show();
                    alert("사용 가능한 아이디입니다.");
                } else {
                    $('#checkmark').hide();
                    alert("이미 사용 중인 아이디입니다.");
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error: ", textStatus, errorThrown);
                alert("아이디 확인에 실패했습니다.");
            }
        });
    });

    $('#user_pwd').on('input', function() {
        var pwd = $(this).val();
        var pwdPattern = /^(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,20}$/;
        if (pwdPattern.test(pwd)) {
            $('#pwdCheckmark').show();
            $('#pwdError').hide();
        } else {
            $('#pwdCheckmark').hide();
            $('#pwdError').show();
        }
    });
});
