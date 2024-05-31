$(function() {
		$('#company_number_btn').on('click',function() {
			var data = $("#company_number").val();
			console.log(data);
			var data1 = {"b_no" : [ data ]};
			console.log(data1);
			debugger;
			
			$.ajax({
				url : "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=cjlBlZd9MpuLsL8wGluvUW%2F2cbWcUcNP7nr%2FuWO7Gbl%2Be7Li1KYbOlQm3cWN0GKnxnFI%2BjSewI3AAMiTKFJk0Q%3D%3D", // serviceKey 값을 xxxxxx에 입력
				type : "POST",
				data : JSON.stringify(data1), // json 을 string으로 변환하여 전송
				dataType : "JSON",
				contentType : "application/json",
				accept : "application/json",
				success : function(result) {
					debugger;
					var data2 = result.data[0].tax_type_cd;
					debugger;
					if (data2 == "") {
						$('#company_number_result').text(
								'등록되지않은 사업자등록번호입니다.')
						debugger;
					} else {
						$('#company_number_result').text(
								'등록된 사업자등록번호입니다.')
						debugger;
					}
				},
				error : function(result) {
					console.log(result.responseText); //responseText의 에러메세지 확인
				}
			});
		});
	});
	
	$(function(){
		$("#company_id_check").on('click',function() {
			var comId = $("#company_id").val();
			debugger;
			
			$.ajax({
				url : "/idcheck.go",
				data :{ id: comId },
				type: "post",
				dataType : "text",
				success : function(result) {
					debugger;
					$("#idcheck_result").text(result);
					debugger;
				},
				
				error : function() {
					alert("데이터 통신 오류입니다.~~~");
				}
			});
			
		});
	});
	
	$(function(){
		$("#mgr_phone_check").on('click',function() {
			var mgrPhone = $("#company_mgr_phone").val();
			debugger;
			
			$.ajax({
				url : "/send_sms.go",
				type : "POST",
				data : { mgrPhone : mgrPhone },
				dataType : "JSON",
				success : function(result) {
					console.log(result);
	                if (result.status == 200) {
	                	$("#verifyBox").show();
	                    alert("인증번호가 전화번호로 전송되었습니다.");
	                    $("#resendbtn").show();
	                    $("#mgr_phone_check").hide();	                    
	                } else {
	                    alert("인증번호 전송에 실패했습니다.");
	                }
				},
				
				error : function() {
					alert("데이터 통신 오류입니다.~~~");
				}
			});
		});
	});
	
	$(function(){
		$("#resendbtn").on('click',function() {
			var mgrPhone = $("#company_mgr_phone").val();
			debugger;
			
			$.ajax({
				url : "/re_send_sms.go",
				type : "POST",
				data : { mgrPhone : mgrPhone },
				dataType : "JSON",
				success : function(result) {
					console.log(result);
	                if (result.status == 200) {
	                    alert("인증번호가 전화번호로 재전송되었습니다.");                   
	                } else {
	                    alert("인증번호 전송에 실패했습니다.");
	                }
				},
				
				error : function() {
					alert("데이터 통신 오류입니다.~~~");
				}
			});
		});
	});
	
	$(function(){
		$("#phone_verify_check").on('click',function() {
			var phone = $("#company_mgr_phone").val();
			var code = $("#phone_verify").val();
			debugger;
			
			$.ajax({
				url : "/smsCodeCheck.go",
				type: "POST",
				dataType: 'json',
		        contentType: 'application/json',
		        data: JSON.stringify({ phone: phone, code: code }),
				success : function(response) {
					debugger;
					if (response.status === "success") {
	                    $('#phone_code_check').text(response.message);
	                } else {
	                    $('#phone_code_check').text(response.message);
	                }
				},
				
				error : function() {
					alert("데이터 통신 오류입니다.~~~");
				}
			});
			
		});
	});
	
	$(function(){
		$('#company_pwd').on('input', function() {
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

	$(function(){
		var pwd_check = false;
		$("#company_pwd_check").focusout(function(){
			pwdCheck($(this).val());
		});
		
		function pwdCheck(pwd){
			if(pwd==""){
				$("#pwd_check").text("");
	            return;
			}
			
			if($("#company_pwd_check").val() === $("#company_pwd").val()){
				$("#pwd_check_ok").show();
				$("#pwd_check_err").hide();
			}else{
				$("#pwd_check_ok").hide();
				$("#pwd_check_err").show();
			}
		};
	});

	function insertCheck(){
		var pwd = $('#company_pwd').val();
		var pwdPattern = /^(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,20}$/;
		
		if($("#company_number_result").text() !== "등록된 사업자등록번호입니다."){
			debugger;
			alert('사업자등록번호를 확인하여주십시오.');
			$("#company_number").focus();
			return false;
		}
		
		if($("#idcheck_result").text() !== "사용가능한 아이디입니다."){
			debugger;
			alert('아이디를 확인하여주십시오.');
			$("#company_id").focus();
			return false;
		}
		
		if(!pwdPattern.test(pwd)){
			debugger;
			alert('비밀번호를 확인하여주십시오.');
			$("#company_pwd").focus();
			return false;
		}
		
		if($("#company_pwd").val()!=$("#company_pwd_check").val()){
			debugger;
			alert('비밀번호 확인을 확인하여주십시오.');
			$("#company_pwd_check").focus();
			return false;
		}
		
		/* if($("#phone_code_check").text() !== "인증 성공"){
			debugger;
			alert('휴대폰 본인인증 번호를 확인하여주십시오.');
			$("#phone_verify").focus();
			return false;
		} */
	};
	
	$(function(){
		$("#logo").on("change", function(event){
			var file = event.target.files[0]
			var maxSize = 5 * 1024 * 1024; 

		    if(file.size > maxSize){
				alert("파일첨부 사이즈는 5MB 이내로 가능합니다.");
				$(this).val(''); 
				return;
		    }
			debugger;
			
			var reader = new FileReader();
			debugger;
			
			reader.onload = function(e){
				$("#preview_logo").attr("src", e.target.result);
			}
			debugger;
			reader.readAsDataURL(file);
			debugger;
			$("#preview_logo").show();
			debugger;
		});
	});
		