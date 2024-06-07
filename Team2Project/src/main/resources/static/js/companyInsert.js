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
						$('#company_number_checkmark').hide();
						alert("비정상적인 사업자 등록번호입니다.");
						debugger;
					} else {
						$.ajax({
							url : "/comNoCheck.go",
							data :{ company_no : data },
							type: "post",
							dataType : "text",
							success : function(result) {
								debugger;
							if(result === "available"){
								debugger;
								$('#company_number_checkmark').show();
		                    	alert("사용 가능한 사업자등록번호입니다.");
							}else{
								$('#company_number_checkmark').hide();
		                    	alert("이미 사용 중인 사업자등록번호입니다.");
							}
						},
						
						error : function() {
							alert("데이터 통신 오류입니다.~~~");
						}
								})
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
					if(result === "available"){
						$('#Idcheckmark').show();
                    	alert("사용 가능한 아이디입니다.");
					}else{
						$('#Idcheckmark').hide();
                    	alert("이미 사용 중인 아이디입니다.");
					}
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
	                	$("#phone_verify").show();
	                	$("#phone_verify_check").show();
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
	                    $('#phone_check_ok_mark').show();
	                    $('#phone_check_err').hide();
	                } else {
	                    $('#phone_check_err').show();
	                    $('#phone_check_ok_mark').hide();
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
		
		var isIdValid = $('#Idcheckmark').is(':visible');
		var isComNoValid = $('#company_number_checkmark').is(':visible');
		var isPhoneCheckValid = $('#phone_check_ok_mark').is(':visible');
		
		var companyAddr = document.getElementById('company_addr').value;
       var companyAddrDetail = document.getElementById('company_addr_detail').value;
       document.getElementById('company_addr').value = companyAddr + ' ' + companyAddrDetail;
       
       debugger;
		
		if(!isComNoValid){
			debugger;
			alert('사업자등록번호를 확인하여주십시오.');
			$("#company_number").focus();
			return false;
		}
		
		if (!isIdValid) {
	        alert("아이디가 유효하지 않습니다.");
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
		
		if(!isPhoneCheckValid){
			debugger;
			alert('휴대폰 본인인증 번호를 확인하여주십시오.');
			$("#phone_verify").focus();
			return false;
		}
		

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
                  document.getElementById('company_addr').value = roadAddr + extraRoadAddr;
                  // 상세주소 필드에 포커스
                  document.getElementById('company_addr_detail').style.display = 'block';
                  document.getElementById('company_addr_detail').focus();
                }
            }).open();
        }
   
 /*  // submit 클릭 시 주소와 상세주소의 필드 값을 결합
   function combineAddress() {
       // 상세주소 값을 주소 필드에 추가
       var companyAddr = document.getElementById('company_addr').value;
       var companyAddrDetail = document.getElementById('company_addr_detail').value;
       document.getElementById('company_addr').value = companyAddr + ' ' + companyAddrDetail;
       return true; // 폼 제출을 계속 진행
	} */

		