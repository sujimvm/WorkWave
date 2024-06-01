// 화면 시작 
$(document).ready(function() {
	$(document).on('click', '#addTempBt', function() {
		addTemp();
	});
});

// 지역 대분류 조회
function addTemp() {
	
	var addForm = $("#addForm").serialize();
	
	$.ajax({
		url: '/comBoard/addTemp',
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