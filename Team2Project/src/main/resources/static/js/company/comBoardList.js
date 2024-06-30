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
			var apply_total_map = viewMap.applyTotalMap; // 각 공고의 총 지원자 수 맵
            var apply_non_check_map = viewMap.applyNonCheckMap;
        	
			// 기존 리스트 제거
            $('#boardListTb').empty();
            $('#boardTempListTb').empty();
            
            appendList('#boardListTb', list, apply_total_map, apply_non_check_map);
            appendTempList('#boardTempListTb', templist);
            
            $('#all-list').on('click', function() {
				displayList('all', list, apply_total_map, apply_non_check_map);
				highlightSelected('#all-list');
			});
			
			$('#end-list').on('click', function() {
				displayList('end', list, apply_total_map, apply_non_check_map);
				highlightSelected('#end-list');
			});
			
			$('#ing-list').on('click', function() {
				displayList('ing', list, apply_total_map, apply_non_check_map);
				highlightSelected('#ing-list');
			});
			
			$('#waiting-list').on('click', function() {
				displayList('wait', list, apply_total_map, apply_non_check_map);
				highlightSelected('#waiting-list');
			});
			
			$('#all-list').click();
		},error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
};

function displayList(status, list, apply_total_map, apply_non_check_map) {
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
	
	appendList('#boardListTb', selectList, apply_total_map, apply_non_check_map);
}

function appendList(selector, list, apply_total_map, apply_non_check_map){
	
	if(list && list.length > 0){
		var row ="<tr><th class='com-board-title-width'>공고제목</th><th class='com-board-apply-width'>총 지원자</th><th class='com-board-apply-width'>미열람 지원자</th></tr>"
		$(selector).append(row);
		
		list.forEach(function(list){
		var totalApplicants = apply_total_map[list.com_board_key]; // 지원자 수가 없을 경우 0으로 설정
    	var nonCheckedApplicants = apply_non_check_map[list.com_board_key]; // 미확인 지원자 수가 없을 경우 0으로 설정
	
		var row = "<tr>" +
				"<td class='com-board-title-width'>"+"<div class='listTitleDiv titleDiv'><a href='/C/comBoardCont?No="+list.com_board_key+"' style='color:#000;'>" + list.com_board_title + "</a></div>" + 
				"<div class='listSubDiv titleDiv'>"+list.com_board_career +"&nbsp;&#124;&nbsp;"+ list.com_board_edu +"&nbsp;&#124;&nbsp;"+"&nbsp;&#124;&nbsp;"+ list.com_board_jobtype  + "</div>" + 
				"<div class='listGroupDiv titleDiv'>"+list.com_board_group +"&nbsp;&gt;&nbsp;"+ list.com_board_sub +"&nbsp;&gt;&nbsp;"+ list.com_board_step + "</div>"+"</td>" +
				"<td class='com-board-apply-width'><a href='/C/totalApply?no="+list.com_board_key+"'>"+totalApplicants+"</a></td>" +
				"<td class='com-board-apply-width'><a href='/C/totalApply?no="+list.com_board_key+"'>"+nonCheckedApplicants+"</td>" +
				"</tr>";
		$(selector).append(row);
	});
	}else{
		var row = "<tr>"+
				  "<td style='text-align: center;'>"+"<span>해당 공고가 없습니다.</span></td></tr>";
		$(selector).append(row);
	}
	
	
}

function appendTempList(selector, templist) {
	
	if(templist && templist.length > 0){
		templist.forEach(function(templist){
		var jobCode = (templist.com_board_group == null)? "미지정":templist.com_board_group +"&nbsp;&gt;&nbsp;"+ templist.com_board_sub +"&nbsp;&gt;&nbsp;"+ templist.com_board_step;
		
		var row = "<tr>" +
				"<td class='com-board-title-width'>"+"<div class='listTitleDiv titleDiv'>" + templist.com_board_title + "</div>" + 
				"<div class='listSubDiv titleDiv'>"+templist.com_board_career +"&nbsp;&#124;&nbsp;"+ templist.com_board_edu +"&nbsp;&#124;&nbsp;"+"&nbsp;&#124;&nbsp;"+ templist.com_board_jobtype  + "</div>" + 
				"<div class='listGroupDiv titleDiv'>"+ jobCode + "</div>"+"</td>"+
				"<td><a href='/C/comBoard/temp?No="+templist.com_board_key+"'>이어서 작성하기</a><a href='javascript:deleteTempComboard("+templist.com_board_key+");' style='margin-left: 20px;'>삭제</a></td>" +
				"</tr>";
		$(selector).append(row);
		});	
	}else {
		var row = "<tr>"+
				  "<td style='text-align: center;'>"+ "<span>현재 작성중인 공고가 없습니다.</span></td></tr>";
		$(selector).append(row);
	}
}



function highlightSelected(element) {
    $('.nav-btn li').removeClass('nav-btn-selected'); // 모든 버튼에서 selected 클래스 제거
    $(element).addClass('nav-btn-selected'); // 클릭된 버튼에 selected 클래스 추가
};

});

function deleteTempComboard(num) {
	if(confirm("임시 공고를 삭제하시겠습니까?")){
		location.href="/C/comBoard/tempDelete?No="+num;
	}
}