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
		
	// 직업분류 0 이면 클릭 시 show , 1이면 클릭 시 hide
	$(document).on('click', '#locationCodeOutputDiv', function() {
		if(locationCodeOutput == 0){
			locationCodeOutput = 1;
			$('#locationCodeTotalDiv').show();
		}else{
			locationCodeOutput = 0;
			$('#locationCodeTotalDiv').hide();
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
	
		
	$(document).on('click', '#locationCodeGroupUl input:checkbox', function() {
	   if ($(this).is(':checked')){
		   $('.locationCodeSubUl').hide();
		   $('.locationCodeStepUl').hide();
		   $('#locationCodeSubUl'+$(this).val()).show();
	  }
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
function getComBoardList() {
	$.ajax({
		url: '/comBoard/list',
		type: 'post',
		dataType: 'json',
		success: function(list) {
			$('#jobListTb').empty(); // Clear any existing rows
			list.forEach(function(list) {
				var row = "<tr>" +
					"<td><input type='checkbox'></td>" + //선택한 공고 한번에 지원하기 체크박스
					"<td><a href='#'>" + list.company_name + "</a></td>" +
					"<td><a href='#'>" + list.com_board_title + "<br>" + 
					list.com_board_career + list.com_board_edu + list.company_addr + list.com_board_type  + "<br>" + 
					list.com_board_group + list.com_board_sub + list.com_board_step + "</a></td>" +
					"<td><input type='checkbox'></td>" + //관심기업 등록 체크박스
					"<td><input type='button' value='지원하기'></td>" + // 해당기업 지원하기
					"</tr>";
				$('#jobListTb').append(row);
			}); 
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}