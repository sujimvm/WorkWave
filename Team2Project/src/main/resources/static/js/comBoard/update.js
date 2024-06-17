// 화면 시작 
$(document).ready(function() {
	
	for(var i=0;i<$("#com_board_jobtype option").length;i++){
		if($("#com_board_jobtype option").eq(i).val() == $("#com_board_jobtype_select").val()){
			$("#com_board_jobtype option").eq(i).attr("selected",true);
			break;
		}
	}

	for(var i=0;i<$("#com_board_week option").length;i++){
		if($("#com_board_week option").eq(i).val() == $("#com_board_week_select").val()){
			$("#com_board_week option").eq(i).attr("selected",true);
			break;
		}
	}
	
	var time = $("#com_board_time").val().split('-');
	var startTime = time[0].split(':');
	var endTime = time[1].split(':');
	
	$("#com_board_time1").val(startTime[0]);
	$("#com_board_time2").val(startTime[1]);
	$("#com_board_time3").val(endTime[0]);
	$("#com_board_time4").val(endTime[1]);
	
	var benefitsCks = $("#com_board_benefits_Checked").val();
	if(benefitsCks != ""){
		var benefitsCk = benefitsCks.split(",");
		benefitsCk.forEach(function(benefitsCk) {
			for(var i=0;i<$('input:checkbox[name="com_board_benefits_Ck"]').length;i++){
				if($('input:checkbox[name="com_board_benefits_Ck"]').eq(i).val() == benefitsCk){
					$('input:checkbox[name="com_board_benefits_Ck"]').eq(i).attr("checked",true);
					break;
				}
			}
		});
	}
	
	for(var i=0;i<$("#com_board_career option").length;i++){
		if($("#com_board_career option").eq(i).val() == $("#com_board_career_select").val()){
			$("#com_board_career option").eq(i).attr("selected",true);
			break;
		}
	}
	
	for(var i=0;i<$("#com_board_edu option").length;i++){
		if($("#com_board_edu option").eq(i).val() == $("#com_board_edu_select").val()){
			$("#com_board_edu option").eq(i).attr("selected",true);
			break;
		}
	}
	
	var conditionsCks = $("#com_board_conditions_Checked").val();
	if(conditionsCks != ""){
		var conditionsCk = conditionsCks.split(",");
		conditionsCk.forEach(function(conditionsCk) {
			for(var i=0;i<$('input:checkbox[name="com_board_conditions_Ck"]').length;i++){
				if($('input:checkbox[name="com_board_conditions_Ck"]').eq(i).val() == conditionsCk){
					$('input:checkbox[name="com_board_conditions_Ck"]').eq(i).attr("checked",true);
					break;
				}
			}
		});
	}
});