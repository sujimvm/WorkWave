// 화면 시작 
$(document).ready(function() {
	$(document).on('click', '#addTempBt', function() {
		addTemp();
	});
	
	$(document).on('click', '#executionBt', function() {
		executionFrom();
	});
	
	// 직업분류 0 이면 클릭 시 show , 1이면 클릭 시 hide (함수호출)
	$(document).on('click', '.jobCodeInput', function() {
		viewJobCodeOutput();
		
		// 체크박스를 라디오버튼으로 변경
		$(".jobCk").removeAttr("type");
		$(".jobCk").attr("type", "radio");
		$(".jobCk_all").closest("li").remove();
	});
	
	$(document).on('click', '.jobCodeStepUl .jobCk_step', function() {
		if ($(".jobCk_step").is(':checked')){
			var step_name = $(this).val().split('/')[1];
			var sub_name = $("#j"+$(this).val().split('/')[0].substr(0,5)).val().split('/')[1];
			var group_name = $("#j"+$(this).val().split('/')[0].substr(0,2)).val().split('/')[1];
			$("#com_board_group").val(group_name);
			$("#com_board_sub").val(sub_name);
			$("#com_board_step").val(step_name);
			$("#jobCodeTotalDiv").hide();
		} 
	});
	
	$(document).on('click', 'input:checkbox[name=com_board_benefits_Ck]', function() {
		if ($('input:checkbox[name=com_board_benefits_Ck]:checked').length > 5){
			alert("5개까지만 선택 가능합니다.");
			$(this).prop("checked",false);
		} 
	});
	
	$(document).on('click', 'input:checkbox[name=com_board_conditions_Ck]', function() {
		if ($('input:checkbox[name=com_board_conditions_Ck]:checked').length > 5){
			alert("5개까지만 선택 가능합니다.");
			$(this).prop("checked",false);
		} 
	});
	
    $("#com_board_mgr_phone").blur(function() {
        // 입력된 전화번호 가져오기
        var phoneNumber = $("#com_board_mgr_phone").val();
        if(phoneNumber) {
            // 정규식을 사용하여 형식 검사
            var regex = /^(01[0-9]{1}-?[0-9]{4}-?[0-9]{4}|01[0-9]{8})$/;

            if (regex.test(phoneNumber)) {
                // 올바른 형식일 경우
				$('#com_board_mgr_phone_CkText').empty();
				$('#com_board_mgr_phone_Ck').val(1);
            } else {
                // 잘못된 형식일 경우
                $('#com_board_mgr_phone_CkText').empty();
				$('#com_board_mgr_phone_CkText').append("연락처를 확인해주세요");
				$('#com_board_mgr_phone_Ck').val(0);
                return false;
            }

            var pcs = phoneNumber;

            // 입력된 문자열에서 하이픈('-')을 제거하여 숫자만 추출
            var pcs = pcs.replace(/[^0-9]/g, '');

            // 전화번호 형식 (010-1234-5678)으로 변환
            if (pcs.length === 10) {
                pcs = pcs.substring(0, 3) + '-' + pcs.substring(3, 7) + '-' + pcs.substring(7, 11);
            } else if (pcs.length === 11) {
                pcs = pcs.substring(0, 3) + '-' + pcs.substring(3, 7) + '-' + pcs.substring(7, 11);
            }

            $("#com_board_mgr_phone").val(pcs);
        }
    });
	
    $("#com_board_mgr_email").blur(function() {
		var email = $("#com_board_mgr_email").val();
		var regexp = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
		if (!regexp.test(email)) {
            $('#com_board_mgr_email_CkText').empty();
			$('#com_board_mgr_email_CkText').append("이메일를 확인해주세요");
			$('#com_board_mgr_email_Ck').val(0);
			return false;
		}else{
            $('#com_board_mgr_email_CkText').empty();
			$('#com_board_mgr_email_Ck').val(1);
		}
    });
        
	ClassicEditor
        .create(document.querySelector('#com_board_cont'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            $('style').append('.ck-content { height: 400px; }');
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#com_board_cont').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });
        
        
	 $(window).scroll(function( ){  //스크롤이 움직일때마다 이벤트 발생 
	      var position = $(window).scrollTop(); // 현재 스크롤바의 위치값을 반환합니다.
	      $("#comBoardBtDiv").stop().animate({top:position+"px"}, 1); //해당 오브젝트 위치값 재설정
	 });
});

// 임시저장
function addTemp() {
	
	var executionFrom = $("#executionFrom").serialize();
	
	$.ajax({
		url: '/ajax/comBoardTemp/insert',
		type: 'post',
		dataType: 'json',
        data: executionFrom,
		success: function(temp_key) {
			$("#temp_key").val(temp_key);
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

// 실행
function executionFrom() {
	
	    
    var com_board_benefits_Ck = [];
    $('input:checkbox[name=com_board_benefits_Ck]:checked').each(function(){
		com_board_benefits_Ck.push($(this).val());
	});
	$("#com_board_benefits").val(com_board_benefits_Ck);
		
    var com_board_conditions_Ck = [];
    $('input:checkbox[name=com_board_conditions_Ck]:checked').each(function(){
		com_board_conditions_Ck.push($(this).val());
	});
	$("#com_board_conditions").val(com_board_conditions_Ck);
	
	if($("#com_board_title").val()==""){
		alert("공고제목를 입력해주세요.");
    	$("#com_board_title").focus();
    	return false;
	}else if($("#com_board_group").val()==""){
		alert("업종를 선택해주세요.");
    	$("#com_board_group").focus();
    	return false;
	}else if($("#com_board_mojib").val()==""){
		alert("모집인원을 입력해주세요.");
    	$("#com_board_mojib").focus();
    	return false;
	}else if($("#com_board_sal").val()==""){
		alert("급여를 입력해주세요.");
    	$("#com_board_sal").focus();
    	return false;
	}else if($("#com_board_time1").val()==""){
		alert("출근시간을 입력해주세요.");
    	$("#com_board_time1").focus();
    	return false;
	}else if($("#com_board_time2").val()==""){
		alert("출근시간을 입력해주세요.");
    	$("#com_board_time2").focus();
    	return false;
	}else if($("#com_board_time3").val()==""){
		alert("퇴근시간을 입력해주세요.");
    	$("#com_board_time3").focus();
    	return false;
	}else if($("#com_board_time4").val()==""){
		alert("퇴근시간을 입력해주세요.");
    	$("#com_board_time4").focus();
    	return false;
	}else if($("#com_board_start_date").val()==""){
		alert("공고 시작일을 선택해주세요.");
    	$("#com_board_start_date").focus();
    	return false;
	}else if($("#com_board_end_date").val()==""){
		alert("공고 마감일을 선택해주세요.");
    	$("#com_board_end_date").focus();
    	return false;
	}else if($("#com_board_mgr_name").val()==""){
		alert("담당자 이름을 입력해주세요.");
    	$("#com_board_mgr_name").focus();
    	return false;
	}else if($("#com_board_mgr_phone").val()==""){
		alert("담당자 연락처를 입력해주세요.");
    	$("#com_board_mgr_phone").focus();
    	return false;
	}else if($("#com_board_mgr_phone_Ck").val()!=1){
		alert("담당자 연락처를 확인해주세요.");
    	$("#com_board_mgr_phone").focus();
    	return false;
	}else if($("#com_board_mgr_email").val()==""){
		alert("담당자 이메일을 입력해주세요.");
    	$("#com_board_mgr_email").focus();
    	return false;
	}else if($("#com_board_mgr_email_Ck").val()!=1){
		alert("담당자 이메일을 확인해주세요.");
    	$("#com_board_mgr_email").focus();
    	return false;
	}

	var com_board_time = $("#com_board_time1").val() +":"+ $("#com_board_time2").val() +"~"+ $("#com_board_time3").val() +":"+ $("#com_board_time4").val(); 
	$("#com_board_time").val(com_board_time);
	
	$("#executionFrom").submit();
}
