let objEditor;

// 화면 시작 
$(document).ready(function() {
	
	ClassicEditor
        .create(document.querySelector('#position_cont'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            $('style').append('.ck-content {height: 300px; }');
            objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#position_cont').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });
});

//포지션제안 상세보기
function view_Modal(key) {
    $("#position_title_view").empty();
    $("#position_cont_view").empty();
    $("#view_Modal").modal();
    $("#position_title_view").append($("#title_"+key).val());
    $("#position_cont_view").append($("#cont_"+key).val());
}

//수정 폼
function update_Modal(key) {
    $("#position_key").val("");
    $("#position_title").val("");
    $("#update_Modal").modal();
    $("#position_key").val(key);
    $("#position_title").val($("#title_"+key).val());
    objEditor.setData($("#cont_"+key).val());
}

function goPositionUpdateModal() {
	if(confirm("포지션제안을 수정 하시겠습니까?")){
		var positionForm = $("#positionForm").serialize();
		
		$.ajax({
			url: '/ajax/position/update',
			type: 'post',
	        data: positionForm,
			success: function() {
				alert("수정이 완료되었습니다");
				location.reload();
			},
			error: function(xhr, status, error) {
				console.error(xhr);
			}
		});
	}
}

function delete_position(position_key) {
	if(confirm("포지션제안을 취소 하시겠습니까?")){
		$.ajax({
			url: '/ajax/position/delete',
			type: 'post',
	        data: {"position_key":position_key},
			success: function() {
				alert("포지션 제안이 취소되었습니다");
				location.reload();
			},
			error: function(xhr, status, error) {
				console.error(xhr);
			}
		});
	}
}

function view_profile(num) {
	window.open("/C/viewProfile?No="+num);
}

function view_comBoard(num) {
	window.open("/C/comBoardCont?No="+num);
}