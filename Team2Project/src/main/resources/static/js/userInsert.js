   
   // 체크 표시로 유효성 검사
   $(document).ready(function() {
   function validateForm() {
	
		// submit 클릭 시 주소와 상세주소의 필드 값을 결합
   
       // 상세주소 값을 주소 필드에 추가
       var userAddr = document.getElementById('user_addr').value;
       var userAddrDetail = document.getElementById('user_addr_detail').value;
       document.getElementById('user_addr').value = userAddr + ' ' + userAddrDetail;

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
       
       // 성별 선택 여부 확인
        var gender = $('#user_gender').val();
        if (gender === "GENDER") {
            console.log("성별을 선택해주세요.");
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
   
   // 이메일 텍스트 입력 후 인증번호 발송 버튼 클릭 시 js 로직
    $('#sendCode').click(function() {
        var email = $('#user_email').val();
        console.log("Sending verification code to: " + email);
        $.ajax({
            url: '/veri/sendCode',
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

   // 인증번호 확인 버튼 클릭 시 확인 여부하는 js 로직
    $('#verifyCode').click(function() {
        var email = $('#user_email').val();
        var code = $('#veri_code').val();
        console.log("Verifying code for: " + email);
        $.ajax({
            url: '/veri/verifyCode',
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

   // DB에 저장된 아이디와 중복여부 체크하는 로직
    $('#checkIdBtn').click(function() {
        var userId = $('#user_id').val();
        console.log("Checking ID for: " + userId);
        $.ajax({
            url: '/ajax/checkUserId',
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

   // 비밀번호 설정 시 필수조건 로직
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

   // 다음 도로명 주소 검색창을 열어주는 로직
   function daumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                    var roadAddr = data.roadAddress; // 도로명 주소 변수
                    var extraRoadAddr = ''; // 참고 항목 변수

                    // 참고항목 문자열이 있을 경우 추가한다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraRoadAddr += data.bname;
                    }
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                       extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if(extraRoadAddr !== ''){
                        extraRoadAddr = ' (' + extraRoadAddr + ')';
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                  document.getElementById('user_addr').value = roadAddr + extraRoadAddr;
                  // 상세주소 필드에 포커스
                  document.getElementById('user_addr_detail').focus();
                }
            }).open();
        }
   
   


