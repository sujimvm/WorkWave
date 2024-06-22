var jobCodeOutput = 0;

// 화면 시작 
$(document).ready(function() {

	getJobCodeGroup();
	
	// 직업분류 0 이면 클릭 시 show , 1이면 클릭 시 hide (함수호출)
	$(document).on('click', '#jobCodeViewDiv', function() {
		viewJobCodeOutput();
	});
	
	$(document).on('click', '#jobCodeGroupUl .jobCk', function(e) {
		
		if ($(this).is(':checked')){
			$('.jobCodeSubUl').hide();
			$('.jobCodeStepUl').hide();
			$('#jobCodeSubUl'+$(this).val().split('/')[0]).show();
		}
		
	});
	$(document).on('click', '.jobCodeSubUl .jobCk', function() {
		
	   if ($(this).is(':checked')){
		   $('.jobCodeStepUl').hide();
		   $('#jobCodeStepUl'+$(this).val().split('/')[0]).show();
	  } 
	});
	$(document).on('click', '.jobCodeStepUl .jobCk_step', function() {
		if ($("#j_all_"+$(this).val().split('/')[0].substr(0,5)).is(':checked')){
			$("#j_all_"+$(this).val().split('/')[0].substr(0,5)).prop("checked",false);
		} 
	});
	
	$(document).on('click', '.jobCodeStepUl .jobCk_all', function() {
		if ($(".jobCk_step").is(':checked')){
			$(".jobCk_step").prop("checked",false);
		} 
	});
	
});

// 직업 대분류 조회 
function getJobCodeGroup() {
	$.ajax({
		url: '/ajax/jobCode',
		type: 'post',
		dataType: 'json',
		success: function(jobCodeList) {
			var jobDataGroup = jobCodeList.group;
			var jobDataSub = jobCodeList.sub;
			var jobDataStep = jobCodeList.step;
			jobDataGroup.forEach(function(group) {
				$('#jobCodeGroupUl').append("<li><input type='checkbox' class='jobCk checkCss' name='g' value='"+group.code+"/"+group.name+"' id='j"+group.code+"'><label for='j"+group.code+"'>" + group.name + "</label></li>");
				$('#jobCodeSubDiv').append("<ul class='jobCodeSubUl codeUl' id='jobCodeSubUl"+group.code+"'></ul>");
				$('#jobCodeSubUl'+group.code).hide();
			}); 
			jobDataSub.forEach(function(sub) {
				$('#jobCodeSubUl'+sub.code.substr(0,2)).append("<li><input type='checkbox' class='jobCk checkCss' name='su' value='"+sub.code+"/"+sub.name+"' id='j"+sub.code+"'><label for='j"+sub.code+"'>" + sub.name + "</label></li>");
				$('#jobCodeStepDiv').append("<ul class='jobCodeStepUl codeUl' id='jobCodeStepUl"+sub.code+"'></ul>");
				$('#jobCodeStepUl'+sub.code).append("<li><input type='checkbox' class='jobCk jobCk_all checkCss jobStep' value='"+sub.code+"' id='j_all_"+sub.code+"'><label for='j_all_"+sub.code+"'>전체</label></li>");
				$('#jobCodeStepUl'+sub.code).hide();
			});
			jobDataStep.forEach(function(step) {
				$('#jobCodeStepUl'+step.code.substr(0,5)).append("<li><input type='checkbox' class='jobCk jobCk_step checkCss jobStep' name='st' value='"+step.code+"/"+step.name+"' id='j"+step.code+"'><label for='j"+step.code+"'>" + step.name + "</label></li>");
			});
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

// 직업분류 0 이면 클릭 시 show , 1이면 클릭 시 hide
function viewJobCodeOutput() {
	if(jobCodeOutput == 0){
		jobCodeOutput = 1;
		$('#jobCodeTotalDiv').show();
	}else{
		jobCodeOutput = 0;
		$('#jobCodeTotalDiv').hide();
	}
}
	