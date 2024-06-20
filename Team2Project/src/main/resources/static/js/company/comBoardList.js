$(document).ready(function() {

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
			list.forEach(function(list) {
				var row = "<tr>" +
					"<td>"+"<div class='listTitleDiv titleDiv'>" + list.com_board_title + "</div>" + 
					"<div class='listSubDiv titleDiv'>"+list.com_board_career +"&nbsp;&#124;&nbsp;"+ list.com_board_edu +"&nbsp;&#124;&nbsp;"+"&nbsp;&#124;&nbsp;"+ list.com_board_jobtype  + "</div>" + 
					"<div class='listGroupDiv titleDiv'>"+list.com_board_group +"&nbsp;&gt;&nbsp;"+ list.com_board_sub +"&nbsp;&gt;&nbsp;"+ list.com_board_step + "</div>"+"</td>" +
					"<td>"+apply_total_list+"</td>" +
					"<td>"+apply_non_check_list+"</td>" +
					"</tr>";
				$('#boardListTb').append(row);
			});
			templist.forEach(function(templist) {
				var row = "<tr>" +
					"<th>"+
					"<div>"+"<span th:text='${session.cDTO.company_name}'>"+"</span>"+"</div>"+
					"<td>"+"<div class='listTitleDiv titleDiv'>" + templist.com_board_title + "</div>" + 
					"<div class='listSubDiv titleDiv'>"+templist.com_board_career +"&nbsp;&#124;&nbsp;"+ templist.com_board_edu +"&nbsp;&#124;&nbsp;"+"&nbsp;&#124;&nbsp;"+ list.com_board_jobtype  + "</div>" + 
					"<div class='listGroupDiv titleDiv'>"+templist.com_board_group +"&nbsp;&gt;&nbsp;"+ templist.com_board_sub +"&nbsp;&gt;&nbsp;"+ list.com_board_step + "</div>"+"</td>" +
					"</tr>";
				$('#boardTempListTb').append(row);
			});
		},error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
};
	getCompanyComboardList(companyKey);
});