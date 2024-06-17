var locationCodeOutput = 0;

// 화면 시작 
$(document).ready(function() {
	getLocationCodeGroup();
	getComBoardList($("#getPage").val());
	
	// 관심기업 추가/삭제
	$(document).on('click', '.interest_check', function() {
		if ($(this).is(':checked')){
			interestCheck(1,$(this).val());
			alert("관심기업 등록이 완료되었습니다");
		}else{
			interestCheck(0,$(this).val());
			alert("관심기업 해제가 완료되었습니다");
		}
	});
	
	//공고 지원
	$(document).on('click', '#all_apply', function() {
		var checked = [];
		$('input:checkbox[name=apply_check]:checked').each(function(){
			checked.push($(this).val());
		});
		addApply(checked);
	});
	
	// 지역 0 이면 클릭 시 show , 1이면 클릭 시 hide
	$(document).on('click', '#locationCodeOutputDiv', function() {
		if(locationCodeOutput == 0){
			locationCodeOutput = 1;
			$('#locationCodeTotalDiv').show();
		}else{
			locationCodeOutput = 0;
			$('#locationCodeTotalDiv').hide();
		}
	});
	$(document).on('click', '#locationCodeGroupUl .locCk', function() {
	   if ($(this).is(':checked')){
		   $('.locationCodeSubUl').hide();
		   $('#locationCodeSubUl'+$(this).val().split('/')[0]).show();
	  }
	});
	
	$(document).on('click', '.locationCodeSubUl .locCk_sub', function() {
		if ($("#l_all_"+$(this).val().split('/')[0].substr(0,2)).is(':checked')){
			$("#l_all_"+$(this).val().split('/')[0].substr(0,2)).prop("checked",false);
		} 
	});
	
	$(document).on('click', '.locationCodeSubUl .locCk_all', function() {
		if ($(".locCk_sub").is(':checked')){
			$(".locCk_sub").prop("checked",false);
		} 
	});
});

// 지역 대분류 조회
function getLocationCodeGroup() {
	$.ajax({
		url: '/ajax/locationCode',
		type: 'post',
		dataType: 'json',
		success: function(locationCodeList) {
			var locationDataGroup = locationCodeList.group;
			var locationDataSub = locationCodeList.sub;
			locationDataGroup.forEach(function(group) {
				$('#locationCodeGroupUl').append("<li><input type='checkbox' class='locCk checkCss' value='"+group.code+"/"+group.name+"' id='l"+group.code+"' name='locationCode'><label for='l"+group.code+"'>" + group.name + "</label></li>");
				$('#locationCodeSubDiv').append("<ul class='locationCodeSubUl codeUl' id='locationCodeSubUl"+group.code+"'></ul>");
				$('#locationCodeSubUl'+group.code).append("<li><input type='checkbox' class='locCk locCk_all checkCss' value='"+group.code+"' id='l_all_"+group.code+"' name='locationCode'><label for='l_all_"+group.code+"'>전체</label></li>");
				$('#locationCodeSubUl'+group.code).hide();
			}); 
				
			locationDataSub.forEach(function(sub) {
				$('#locationCodeSubUl'+sub.code.substr(0,2)).append("<li><input type='checkbox' class='locCk locCk_sub checkCss' value='"+sub.code+"/"+sub.name+"' id='l"+sub.code+"' name='locationCode'><label for='l"+sub.code+"'>" + sub.name + "</label></li>");
			});
			
			
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

// 공고리스트 조회 및 출력
function getComBoardList(nowPg) {
	if(nowPg == '') nowPg = 1;
	$.ajax({
		url: '/ajax/comBoardList',
		type: 'post',
		dataType: 'json',
		data:{"page":nowPg},
		success: function(map) {
			$('#jobListTb').empty(); // Clear any existing rows
			$('#pagination').empty();
			var list = map.list;
			var interestList = map.interestList;
			var applyList = map.applyList;
			var paging = map.paging;
			var paging_li = "";
			list.forEach(function(list) {
				var row = "<tr>" +
					"<td><input type='checkbox' name='apply_check' id='apply_check_"+list.com_board_key+"' value='"+list.com_board_key+"'><a href='#'>" + list.company_name + "</a><input type='checkbox' class='interest_check' name='interest_check_"+list.company_key+"' value='"+list.company_key+"'></td>" + // 선택 지원 | 기업명 | 관심기업 체크박스
					"<td><a href='/A/comBoard/content?P="+nowPg+"&No="+list.com_board_key+"'>" + list.com_board_title + "<br>" + 
					list.com_board_career + list.com_board_edu + list.company_addr + list.com_board_jobtype  + "<br>" + 
					list.com_board_group + list.com_board_sub + list.com_board_step + "</a></td>" +
					"<td><input type='button' id='addApplyBt_"+list.com_board_key+"' onclick='addApply("+list.com_board_key+")' value='지원하기'></td>" + // 해당기업 지원하기
					"</tr>";
				$('#jobListTb').append(row);
				
			}); 
			if(interestList != undefined){
				interestList.forEach(function(interestList) {
					$('input:checkbox[name="interest_check_'+interestList+'"]').attr("checked",true);
				});
			}
			if(applyList != undefined){
				applyList.forEach(function(applyList) {
					$('#apply_check_'+applyList).attr("disabled", true);
					$('#addApplyBt_'+applyList).attr("value", "지원완료");
					$('#addApplyBt_'+applyList).attr("disabled", true);
				});
			}
			
			if(paging != undefined && paging.page > paging.block){
				paging_li += "<li><a href='javascript:getComBoardList(1)'>맨처음</a></li>";
			}
			if(paging != undefined && paging.page > paging.block){ 
				paging_li += "<li><a href='javascript:getComBoardList("+(paging.startBlock-1)+")'> ← </a></li>";
			}
			for(var i = paging.startBlock; i<= paging.endBlock; i++){ 
				paging_li += "<li><a href='javascript:getComBoardList("+i+")'>"+i+"</a></li>";
			}
			if(paging != undefined && paging.endBlock < paging.allPage){ 
				paging_li += "<li><a href='javascript:getComBoardList("+(paging.endBlock+1)+")'> → </a></li>";
			}
			if(paging != undefined && paging.endBlock < paging.allPage){ 
				paging_li += "<li><a href='javascript:getComBoardList("+paging.allPage+")'>맨뒤</a></li>";
			}
		
			$('#pagination').append(paging_li);
		
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

// 관심기업 체크 시 관심기업 등록 / 해제
function interestCheck(check, company_key) {
	
	$.ajax({
		url: '/ajax/interest/action',
		type: 'post',
		dataType: 'json',
		data:{"check":check,"company_key":company_key},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
	getComBoardList($("#getPage").val());
}



// 공고 지원 등록 / 해제
function addApply(checked) {
	
	$.ajax({
		url: '/ajax/apply/insert',
		type: 'post',
		dataType: 'json',
		data:{"checked":checked},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
	alert("지원이 완료되었습니다");
	getComBoardList($("#getPage").val());
}