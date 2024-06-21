$(document).ready(function() {

getCompanyComboardList(companyKey);

function getCompanyComboardList(companyKey) {
	
	console.log(companyKey); // 서버에서 전달된 세션 값
	
	$.ajax({
		url: '/ajax/companyComBoard',
		type: 'get',
		dataType: 'json',
		data: {'companyKey': companyKey},
		success: function(viewMap) {
			var list = viewMap.comBoardList;
			var templist = viewMap.tempList;
			var apply_total_list = viewMap.applyTotalList; // 각 공고의 총 지원자 수 리스트
        	var apply_non_check_list = viewMap.applyNonCheckList;
        	
			// 기존 리스트 제거
            $('#boardListTb').empty();
            $('#boardTempListTb').empty();
            
            appendList('#boardListTb', list, apply_total_list, apply_non_check_list);
            appendTempList('#boardTempListTb', templist);
            
            $('#all-list').on('click', function() {
				displayList('all', list, apply_total_list, apply_non_check_list);
			});
			
			$('#end-list').on('click', function() {
				displayList('end', list, apply_total_list, apply_non_check_list);
			});
			
			$('#ing-list').on('click', function() {
				displayList('ing', list, apply_total_list, apply_non_check_list);
			});
			
			$('#waiting-list').on('click', function() {
				displayList('wait', list, apply_total_list, apply_non_check_list);
			});
			
			$('#all-list').click();
		},error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
};

function displayList(status, list, apply_total_list, apply_non_check_list) {
	$('#boardListTb').empty();
	
	var selectList = list.filter(function(list) {
		var currentDate = new Date();
		var startDate = new Date(list.com_board_start_date);
		var endDate = new Date(list.com_board_end_date);
		
		if(status == 'end'){
			return currentDate > endDate;
		}else if(status == 'ing'){
			return currentDate <= endDate && currentDate >= startDate;
		}else if(status == 'wait'){
			return currentDate < startDate;
		}else{
			return true;
		}
	});
	
	appendList('#boardListTb', selectList, apply_total_list, apply_non_check_list);
}

function appendList(selector, list, apply_total_list, apply_non_check_list){
	list.forEach(function(list, index){
		var row = "<tr>" +
				"<td>"+"<div class='listTitleDiv titleDiv'>" + list.com_board_title + "</div>" + 
				"<div class='listSubDiv titleDiv'>"+list.com_board_career +"&nbsp;&#124;&nbsp;"+ list.com_board_edu +"&nbsp;&#124;&nbsp;"+"&nbsp;&#124;&nbsp;"+ list.com_board_jobtype  + "</div>" + 
				"<div class='listGroupDiv titleDiv'>"+list.com_board_group +"&nbsp;&gt;&nbsp;"+ list.com_board_sub +"&nbsp;&gt;&nbsp;"+ list.com_board_step + "</div>"+"</td>" +
				"<td><a href='/C/totalApply?no="+list.com_board_key+"'>"+apply_total_list[index]+"</a></td>" +
				"<td><a href='/C/totalApply?no="+list.com_board_key+"'>"+apply_non_check_list[index]+"</td>" +
				"</tr>";
		$(selector).append(row);
	});
}

function appendTempList(selector, templist) {
	templist.forEach(function(templist){
		var row = "<tr>" +
				"<td>"+"<div class='listTitleDiv titleDiv'>" + templist.com_board_title + "</div>" + 
				"<div class='listSubDiv titleDiv'>"+templist.com_board_career +"&nbsp;&#124;&nbsp;"+ templist.com_board_edu +"&nbsp;&#124;&nbsp;"+"&nbsp;&#124;&nbsp;"+ templist.com_board_jobtype  + "</div>" + 
				"<div class='listGroupDiv titleDiv'>"+templist.com_board_group +"&nbsp;&gt;&nbsp;"+ templist.com_board_sub +"&nbsp;&gt;&nbsp;"+ templist.com_board_step + "</div>"+"</td>"+
				"<td>"+"<input type='button' onclick='tempComBoard()' value='이어서 작성하기'>"+"</td>" +
				"</tr>";
		$(selector).append(row);
	});
}

// 버튼 클릭 시 작성중 공고로 
/*function tempComBoard() {
	$.ajax(function(){
		url: '/C/comBoard/content?No=temp_key'
		data: 'get'
		type: 
		
	})
}*/

});