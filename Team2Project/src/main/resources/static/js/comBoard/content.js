// 화면 시작 
$(document).ready(function() {
	getRecommendList(1); // 추천 인재
	
	ClassicEditor
        .create(document.querySelector('#position_cont'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            $('style').append('.ck-content {height: 400px; }');
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#position_cont').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });
});

//포지션제안 
function positionInputModal(key,name) {
    $("#recommendModal").modal();
    $("#user_key").val(key);
    $("#user_name").val(name);
}
function goPositionInputModal() {
	if(confirm($("#user_name").val()+"님께 포지션제안을 하시겠습니까?")){
		var positionForm = $("#positionForm").serialize();
		
		$.ajax({
			url: '/ajax/position/insert',
			type: 'post',
	        data: positionForm,
			success: function() {
				alert("포지션제안이 완료되었습니다");
			},
			error: function(xhr, status, error) {
				console.error(xhr);
			}
		});
	}
}

function test(num) {
window.open("/CU/profile/content?no="+num);
}


// 공고리스트 조회 및 출력
function getRecommendList(page) {
	
	$.ajax({
		url: '/ajax/recommendList',
		type: 'post',
		dataType: 'json',
		data:{"page":page,"code":$('#code').val(),"comBoardKey":$('#com_board_key').val()},
		success: function(map) {
			$('#recommendList').empty(); // Clear any existing rows
			$('#pagination').empty();
			var list = map.list;
			var successList = map.successList;
			var paging = map.paging;
			var paging_li = "";
			list.forEach(function(list) {
				var row = "<tr>"+
				"<td rowspan='3'><img src='/image/profile/"+ list.profile_image +"'></td>"+
				"<td>"+ list.user_name +"</td>"+
				"<td rowspan='3'><input type='button' id='positionBt_"+ list.user_key +"' value='포지션제안' onclick='positionInputModal("+ list.user_key +",\""+ list.user_name +"\")'></td>"+
				"</tr>"+
				"<tr><td><a href='javascript:test(\""+ list.profile_key +"\")'>"+ list.profile_name +"</a></td></tr>"+
				"<tr><td>"+ list.profile_group1 +list.profile_sub1 +list.profile_step1 +list.profile_group2 +list.profile_sub2 +list.profile_step2 +"</td></tr>";
				
				$('#recommendList').append(row);	
			}); 
			
			if(successList != undefined){
				successList.forEach(function(successList) {
					$('#positionBt_'+successList).attr("value", "요청완료");
					$('#positionBt_'+successList).attr("disabled", true);
				});
			}
			
			if(paging != undefined && paging.page > paging.block){
				paging_li += "<li><a href='javascript:getRecommendList(1)'>맨처음</a></li>";
			}
			if(paging != undefined && paging.page > paging.block){ 
				paging_li += "<li><a href='javascript:getRecommendList("+(paging.startBlock-1)+")'> ← </a></li>";
			}
			for(var i = paging.startBlock; i<= paging.endBlock; i++){ 
				paging_li += "<li><a href='javascript:getRecommendList("+i+")'>"+i+"</a></li>";
			}
			if(paging != undefined && paging.endBlock < paging.allPage){ 
				paging_li += "<li><a href='javascript:getRecommendList("+(paging.endBlock+1)+")'> → </a></li>";
			}
			if(paging != undefined && paging.endBlock < paging.allPage){ 
				paging_li += "<li><a href='javascript:getRecommendList("+paging.allPage+")'>맨뒤</a></li>";
			}
		
			$('#pagination').append(paging_li);
		
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

