var locationCodeOutput = 0;
var scrollNum = 0;
var prevPage = 1;

// 화면 시작 
$(document).ready(function() {
	getLocationCodeGroup();
	getComBoardList($("#getPage").val());
	
	$(window).scroll(function () { 
		scrollNum = $(document).scrollTop();
	});
	
	//직업분류 소분류 클릭 시 값 넘기기
	$(document).on('click', '.jobCodeStepUl .jobCk_step', function() {
		// 체크박스 값 처리;
		var jobCode = [];
	    $(".jobCk_step:checked").each(function(){
			jobCode.push($(this).val().split('/')[1]);
		});
		
		if ($(".jobCk_step").is(':checked')){
			var step_name = $(this).val().split('/')[1];
			var sub_name = $("#j"+$(this).val().split('/')[0].substr(0,5)).val().split('/')[1];
			var group_name = $("#j"+$(this).val().split('/')[0].substr(0,2)).val().split('/')[1];
			
			$("#jobCodeViewDiv").append("<span>"+group_name+">"+sub_name+">"+step_name+"<span>X</span></span>");
			
		} 
		$("#jobCode").val(jobCode);
	});
	$(document).on('click', '#jobCodeViewDiv span span', function() {
		
		jobCodeOutput = 1;
		$('#jobCodeTotalDiv').hide();
		$(this).closest("span").parent("span").remove();
	});
	
	$(document).on('click', '.locationCodeSubUl .locCk_sub', function() {
		// 체크박스 값 처리;
		var locCode = [];
	    $(".locCk_sub:checked").each(function(){
			locCode.push($(this).val().split('/')[1]);
		});
		
		if ($(".locCk_sub").is(':checked')){
			var sub_name = $(this).val().split('/')[1];
			var group_name = $("#l"+$(this).val().split('/')[0].substr(0,2)).val().split('/')[1];
			
			$("#locCodeViewDiv").append("<span>"+group_name+">"+sub_name+"<span>X</span></span>");
			
		} 
		$("#locCode").val(locCode);
	});
	$(document).on('click', '#locCodeViewDiv span span', function() {
		
		locationCodeOutput = 1;
		$('#locationCodeTotalDiv').hide();
		$(this).closest("span").parent("span").remove();
	});
		
	// 관심기업 추가/삭제
	$(document).on('click', '.interest_check', function() {
		
		if($("#sessionID").val() == 1){
			alert("개인회원으로 로그인 시 이용가능합니다.");
			$(this).prop("checked",false);
		}else if($("#sessionID").val() != undefined){
			if ($(this).is(':checked')){
				interestCheck(1,$(this).val());
			}else{
				interestCheck(0,$(this).val());
			}
			getComBoardList($("#getPage").val());
		}else{
			alert("로그인 후 이용가능합니다.");
			$(this).prop("checked",false);
		}
	});
	
	//공고 지원
	$(document).on('click', '#all_apply', function() {
		if($("#sessionID").val() != undefined){
			var checked = [];
			$('input:checkbox[name=apply_check]:checked').each(function(){
				checked.push($(this).val());
			});
			addApply(checked);
		}else if($("#sessionID").val() == 1){
			alert("개인회원으로 로그인 시 이용가능합니다.");
		}else{
			alert("로그인 후 이용가능합니다.");
		}
	});
	
	// 지역 0 이면 클릭 시 show , 1이면 클릭 시 hide
	$(document).on('click', '#locCodeViewDiv', function() {
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
	
	
	$(document).on('click', 'input:checkbox[name=eduCode]', function() {
		if ($('input:checkbox[name=eduCode]:checked').length > 4){
			alert("4개까지만 선택 가능합니다.");
			$(this).prop("checked",false);
		}else{
			var eduCode_Ck = [];
			$('input:checkbox[name=eduCode]:checked').each(function(){
				eduCode_Ck.push($(this).val());
			});
			$('#eduCode_checked').val(eduCode_Ck);
		}
	});
	$(document).on('click', 'input:checkbox[name=careerCode]', function() {
		if ($('input:checkbox[name=careerCode]:checked').length > 4){
			alert("4개까지만 선택 가능합니다.");
			$(this).prop("checked",false);
		}else{
			var careerCode_Ck = [];
		    $('input:checkbox[name=careerCode]:checked').each(function(){
				careerCode_Ck.push($(this).val());
			});
			$('#careerCode_checked').val(careerCode_Ck);
		}
	});
	$(document).on('click', 'input:checkbox[name=typeCode]', function() {
		if ($('input:checkbox[name=typeCode]:checked').length > 4){
			alert("4개까지만 선택 가능합니다.");
			$(this).prop("checked",false);
		}else{
		    var typeCode_Ck = [];
		    $('input:checkbox[name=typeCode]:checked').each(function(){
				typeCode_Ck.push($(this).val());
			});
			$('#typeCode_checked').val(typeCode_Ck);
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
	$("#getPage").val(nowPg);
	if(nowPg == '') nowPg = 1;
	
	$.ajax({
		url: '/ajax/comBoardList',
		type: 'post',
		dataType: "JSON",
		data: {"page": nowPg,
				"keyword": $('#keyword').val(),
				"jobCode": $('#jobCode').val(),
				"locCode": $('#locCode').val(),
				"eduCode": $('#eduCode_checked').val(),
				"careerCode": $('#careerCode_checked').val(),
				"typeCode": $('#typeCode_checked').val()},
		success: function(map) {
			$('#jobListTb').empty(); // Clear any existing rows
			$('#pagination').empty();
			$("#total").empty();
			$("#pageInfo").empty();
			var list = map.list;
			var interestList = map.interestList;
			var applyList = map.applyList;
			var paging = map.paging;
			var paging_li = "";
			
			$("#total").append(paging.totalRecord+" 건");
			$("#pageInfo").append("전체 "+paging.allPage+"페이지 중 "+paging.page+"페이지");
			
			list.forEach(function(list) {
				var row = "<tr>" +
					"<th>"+
					"<div class='applyCheckDiv'><input type='checkbox' name='apply_check' class='apply_check' id='apply_check_"+list.com_board_key+"' value='"+list.com_board_key+"'>"+
					"<label for='apply_check_"+list.com_board_key+"' title='" + list.company_name + "'>" + list.company_name + "</label></div>"+
					"<div class='interestCheckDiv'><input type='checkbox' id='interestIcon_"+list.company_key+"' class='interest_check' name='interest_check_"+list.company_key+"' value='"+list.company_key+"'>"+
					"<label for='interestIcon_"+list.company_key+"'></label></div>"+
					"</th>" + // 선택 지원 | 기업명 | 관심기업 체크박스
					"<td><a href='/A/comBoard/content?P="+nowPg+"&No="+list.com_board_key+"' title='" + list.com_board_title + "'><div class='listTitleDiv titleDiv'>" + list.com_board_title + "</div></a>" + 
					"<div class='listSubDiv titleDiv'>"+list.com_board_career +"&nbsp;&#124;&nbsp;"+ list.com_board_edu +"&nbsp;&#124;&nbsp;"+ list.company_addr +"&nbsp;&#124;&nbsp;"+ list.com_board_jobtype  + "</div>" + 
					"<div class='listGroupDiv titleDiv'>"+list.com_board_group +"&nbsp;&gt;&nbsp;"+ list.com_board_sub +"&nbsp;&gt;&nbsp;"+ list.com_board_step + "</div></td>" +
					"<td width='15%' style='text-align: center;'><input type='button' class='btCss4' id='addApplyBt_"+list.com_board_key+"' onclick='addApply("+list.com_board_key+",\""+ list.company_name +"\")' value='지원하기'></td>" + // 해당기업 지원하기
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
					$('#addApplyBt_'+applyList).attr("class", "btCss_x");
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
			
			if($("#getPage").val()=='' || $("#getPage").val()==prevPage){
				$('html').scrollTop(scrollNum);
			}else{
				$('html').scrollTop('860');
			}
			
			prevPage = paging.page;
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
		data:{"check":check,"company_key":company_key},
		success: function() {
		},error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}


// 공고 지원 등록 / 해제
function addApply(checked,companyName) {
	var toCompany = "";
	if(companyName != undefined){
		toCompany = companyName+"에 ";
	}
	
	if(confirm(toCompany+"지원을 하시겠습니까?")){
		$.ajax({
			url: '/ajax/apply/insert',
			type: 'post',
			data:{"checked":checked},
			success: function() {
				alert("공고 지원이 완료되었습니다");
				getComBoardList($("#getPage").val());
			},error: function(xhr, status, error) {
				console.error(xhr);
				alert("로그인 후 지원가능합니다.");
			}
		});
	}
}