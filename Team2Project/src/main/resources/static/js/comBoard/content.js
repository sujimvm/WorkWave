// 화면 시작 
$(document).ready(function() {
	getRecommendList(1); // 추천 인재
	
	ClassicEditor
        .create(document.querySelector('#position_cont'), {
            removePlugins: ['Heading'],
            language: "ko"
        })
        .then(editor => {
            $('style').append('.ck-content {height: 300px; }');
            let objEditor = editor;

            editor.model.document.on('change:data', () => {
                $('#position_cont').val(editor.getData());
            }, {priority: 'high'});
        })
        .catch(error => {
            console.error(error);
        });
        
        	// 관심기업 추가/삭제
	$(document).on('click', '#interest_check', function() {
		
		if($("#sessionID").val() == 1){
			alert("개인회원으로 로그인 시 이용가능합니다.");
			$(this).prop("checked",false);
		}else if($("#sessionID").val() != undefined){
			if ($(this).is(':checked')){
				interestCheck(1,$(this).val());
			}else{
				interestCheck(0,$(this).val());
			}
		}else{
			alert("로그인 후 이용가능합니다.");
			$(this).prop("checked",false);
		}
	});
});

// 관심기업 체크 시 관심기업 등록 / 해제
function interestCheck(check, company_key) {
	
	$.ajax({
		url: '/ajax/interest/action',
		type: 'post',
		data:{"check":check,"company_key":company_key},
		success: function() {
		},error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}


//포지션제안 
function positionInputModal(key,name) {
	$("#position_title").val("");
    $("#position_cont").val("");
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
				location.reload();
			},
			error: function(xhr, status, error) {
				console.error(xhr);
			}
		});
	}
}

function profileView(num) {
window.open("/CU/profile/content?no="+num);
}


// 공고리스트 조회 및 출력
function getRecommendList(page) {
	
	if($('#sessionID').val()==1){
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
				if(list.length > 0){
					list.forEach(function(list) {
						var gender = (list.user_gender == 'M')? '남':'여';
						const today = new Date();
						const birthday = new Date(list.user_birth);
						let age = today.getFullYear() - birthday.getFullYear();
						
						var row = "<tr>"+
						"<td width='13%'><img src='/image/profile/"+ list.profile_image +"'></td>"+
						"<td>"+
						"<a href='javascript:profileView(\""+ list.profile_key +"\")'>"+ list.profile_name +"</a>"+
						"<p>"+ list.user_name +"("+age+") "+ gender + "</p>"+
						"<p>"+ list.profile_group1 +list.profile_sub1 +list.profile_step1 +list.profile_group2 +list.profile_sub2 +list.profile_step2 +"</p>"+
						"</td>"+
						"<td width='12%'><input type='button' class='btCss4' id='positionBt_"+ list.user_key +"' value='포지션제안' onclick='positionInputModal("+ list.user_key +",\""+ list.user_name +"\")'></td>"+
						"</tr>";
						
						$('#recommendList').append(row);	
					}); 
					
					if(successList != undefined){
						successList.forEach(function(successList) {
							$('#positionBt_'+successList).attr("value", "요청완료");
							$('#positionBt_'+successList).attr("disabled", true);
						});
					}
				}else{
					$('#recommendList').append("<tr><th width='100%'>추천인재가 존재하지 않습니다.<th></tr>");
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
}

function updateTimer() {
    const future = Date.parse($("#com_board_end_date").val());
    const now = new Date();
    const diff = future - now;

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const mins = Math.floor(diff / (1000 * 60));
    const secs = Math.floor(diff / 1000);

    const d = days;
    const h = String(hours - days * 24).padStart(2, '0');
    const m = String(mins - hours * 60).padStart(2, '0');
    const s = String(secs - mins * 60).padStart(2, '0');

    document.getElementById("timer").innerHTML = '<div>' + d + '일 ' + h + ':' + m + ':' + s + '</div>';
}

setInterval(updateTimer, 1000);
