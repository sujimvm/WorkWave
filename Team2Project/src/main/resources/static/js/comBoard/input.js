// 화면 시작 
$(document).ready(function() {
	
	$(document).on('click', '#addTempBt', function() {
		addTemp();
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
		} 
	});
	
});

// 임시저장
function addTemp() {
	
	var addForm = $("#addForm").serialize();
	
	$.ajax({
		url: '/ajax/comBoardTemp/insert',
		type: 'post',
		dataType: 'json',
        data: addForm,
		success: function(temp_key) {
			$("#temp_key").val(temp_key);
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}