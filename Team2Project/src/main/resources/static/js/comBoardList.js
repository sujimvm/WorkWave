// 화면 시작 
$(document).ready(function() {
	var jobCodeOutput = 0;
	var locationCodeOutput = 0;

	$('#jobCodeTotalDiv').hide();
	$('#locationCodeTotalDiv').hide();
	
	getJobCodeGroup();
	getLocationCodeGroup();
	getComBoardList();
	
	

	// 직업분류 0 이면 클릭 시 show , 1이면 클릭 시 hide
	$(document).on('click', '#jobCodeOutputDiv', function() {
		if(jobCodeOutput == 0){
			jobCodeOutput = 1;
			$('#jobCodeTotalDiv').show();
		}else{
			jobCodeOutput = 0;
			$('#jobCodeTotalDiv').hide();
		}
	});
	$(document).on('click', '#jobCodeGroupUl input:checkbox', function() {
	   if ($(this).is(':checked')){
		   $('.jobCodeSubUl').hide();
		   $('.jobCodeStepUl').hide();
		   $('#jobCodeSubUl'+$(this).val()).show();
	  }
	});
	$(document).on('click', '.jobCodeSubUl input:checkbox', function() {
	   if ($(this).is(':checked')){
		   $('.jobCodeStepUl').hide();
		   $('#jobCodeStepUl'+$(this).val()).show();
	  } 
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
	$(document).on('click', '#locationCodeGroupUl input:checkbox', function() {
	   if ($(this).is(':checked')){
		   $('.locationCodeSubUl').hide();
		   $('.locationCodeStepUl').hide();
		   $('#locationCodeSubUl'+$(this).val()).show();
	  }
	});
	
	// 관심기업 추가/삭제
	$(document).on('click', '.interest_check', function() {
		if ($(this).is(':checked')) interestCheck(1,$(this).val());
		else interestCheck(0,$(this).val());
	});
	
	//공고 지원
	$(document).on('click', '#all_apply', function() {
		var checked = [];
		$('input:checkbox[name=apply_check]:checked').each(function(){
			checked.push($(this).val());
		});
		alert(checked);
		addApply(checked);
	});
});

// 직업 대분류 조회 
function getJobCodeGroup() {
	$.ajax({
		url: '/comBoard/jobCode',
		type: 'post',
		dataType: 'json',
		success: function(jobCodeList) {
			var jobDataGroup = jobCodeList.group;
			var jobDataSub = jobCodeList.sub;
			var jobDataStep = jobCodeList.step;
			jobDataGroup.forEach(function(group) {
				$('#jobCodeGroupUl').append("<li><input type='checkbox' value='"+group.code+"' id='j"+group.code+"' name='jobCode'><label for='j"+group.code+"'>" + group.name + "</label></li>");
				$('#jobCodeSubDiv').append("<ul class='jobCodeSubUl' id='jobCodeSubUl"+group.code+"'></ul>");
				$('#jobCodeSubUl'+group.code).hide();
			}); 
			jobDataSub.forEach(function(sub) {
				$('#jobCodeSubUl'+sub.code.substr(0,2)).append("<li><input type='checkbox' value='"+sub.code+"' id='j"+sub.code+"' name='jobCode'><label for='j"+sub.code+"'>" + sub.name + "</label></li>");
				$('#jobCodeStepDiv').append("<ul class='jobCodeStepUl' id='jobCodeStepUl"+sub.code+"'></ul>");
				$('#jobCodeStepUl'+sub.code).append("<li><input type='checkbox' value='"+sub.code+"' id='j"+sub.code+"' name='jobCode'><label for='j"+sub.code+"'>전체</label></li>");
				$('#jobCodeStepUl'+sub.code).hide();
			});
			jobDataStep.forEach(function(step) {
				$('#jobCodeStepUl'+step.code.substr(0,3)).append("<li><input type='checkbox' value='"+step.code+"' id='j"+step.code+"' name='jobCode'><label for='j"+step.code+"'>" + step.name + "</label></li>");
			});
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

// 지역 대분류 조회
function getLocationCodeGroup() {
	$.ajax({
		url: '/comBoard/locationCode',
		type: 'post',
		dataType: 'json',
		success: function(locationCodeList) {
			var locationDataGroup = locationCodeList.group;
			var locationDataSub = locationCodeList.sub;
			var locationDataStep = locationCodeList.step;
			locationDataGroup.forEach(function(group) {
				$('#locationCodeGroupUl').append("<li><input type='checkbox' value='"+group.code+"' id='l"+group.code+"' name='locationCode'><label for='l"+group.code+"'>" + group.name + "</label></li>");
				$('#locationCodeSubDiv').append("<ul class='locationCodeSubUl' id='locationCodeSubUl"+group.code+"'></ul>");
				$('#locationCodeSubUl'+group.code).append("<li><input type='checkbox' value='"+group.code+"' id='l"+group.code+"' name='locationCode'><label for='i"+group.code+"'>전체</label></li>");
				$('#locationCodeSubUl'+group.code).hide();
			}); 
				
			locationDataSub.forEach(function(sub) {
				$('#locationCodeSubUl'+sub.code.substr(0,2)).append("<li><input type='checkbox' value='"+sub.code+"' id='l"+sub.code+"' name='locationCode'><label for='l"+sub.code+"'>" + sub.name + "</label></li>");
			});
			
			
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}
	
// 공고리스트 조회 및 출력
function getComBoardList(nowPg) {
	if(nowPg == undefined) nowPg = 1;
	$.ajax({
		url: '/comBoard/list',
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
			th:href="@{/comBoard/add}"
			list.forEach(function(list) {
				var row = "<tr>" +
					"<td><input type='checkbox' name='apply_check' id='apply_check_"+list.com_board_key+"' value='"+list.com_board_key+"'><a href='#'>" + list.company_name + "</a><input type='checkbox' class='interest_check' name='interest_check_"+list.company_key+"' value='"+list.company_key+"'></td>" + // 선택 지원 | 기업명 | 관심기업 체크박스
					"<td><a href='/comBoard/content?page="+nowPg+"&No="+list.com_board_key+"'>" + list.com_board_title + "<br>" + 
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
		url: '/comBoard/interestCheck',
		type: 'post',
		dataType: 'json',
		data:{"check":check,"company_key":company_key},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
	getComBoardList();
}

// 공고 지원 등록 / 해제
function addApply(checked) {
	$.ajax({
		url: '/comBoard/addApply',
		type: 'post',
		dataType: 'json',
		data:{"checked":checked},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
	getComBoardList();
}