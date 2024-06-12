// 화면 시작 
$(document).ready(function() {
	getComBoardList();
});

// 공고리스트 조회 및 출력
function getComBoardList() {
	$.ajax({
		url: '/ajax/mainComBoardList',
		type: 'post',
		dataType: 'json',
		success: function(map) {
			$('#mainJobTimeListTb').empty(); // Clear any existing rows
			$('#mainJobHotListTb').empty(); // Clear any existing rows
			$('#mainJobNewListTb').empty(); // Clear any existing rows
			var time = map.time;
			var hot = map.hot;
			var newL = map.new;
			
			var timeTd = "<tr>";
			var hotTd = "<tr>";
			var newTd = "<tr>";
			
			var timeEmpty = 10 - time.length;
			var hotEmpty = 10 - hot.length;
			var newEmpty = 10 - newL.length;
			
			/* 마감 공고 */
			time.forEach(function(time,index) {
				if(index == 5) timeTd += "</tr><tr>";
				timeTd += "<td onclick=\"location.href='/A/comBoard/content?P=1&No=" + time.com_board_key + "'\"><div class='mainListTbDiv'>";
				if(time.company_logo != null) timeTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/"+ time.company_logo +"'></div>";
				if(time.company_logo == null) timeTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/main_logo.png'></div>";
				
				
				
				
				
				timeTd += "</div></td>";
			}); 
			
			if(timeEmpty > 0) for(var i=0;i<timeEmpty;i++) timeTd += "<td><div class='mainListTbDiv'>비어있을 경우</div></td>";
			timeTd += "</div></td>";
			
			$('#mainJobTimeListTb').append(timeTd +"</tr>");
				
				
				
				
			/* 인기 공고 */
			hot.forEach(function(hot,index) {
				if(index == 5) hotTd += "</tr><tr>";
				hotTd += "<td onclick=\"location.href='/A/comBoard/content?P=1&No=" + hot.com_board_key + "'\"><div class='mainListTbDiv'>";
				if(hot.company_logo != null) hotTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/"+ hot.company_logo +"'></div>";
				if(hot.company_logo == null) hotTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/main_logo.png'></div>";
				
				
				
				
				
				hotTd += "</div></td>";
			}); 
			
			if(hotEmpty > 0) for(var i=0;i<hotEmpty;i++) hotTd += "<td><div class='mainListTbDiv'>비어있을 경우</div></td>";
			hotTd += "</div></td>";
			
			$('#mainJobHotListTb').append(hotTd +"</tr>");
				
				
			/* 최신 공고 */
			newL.forEach(function(newL,index) {
				if(index == 5) newTd += "</tr><tr>";
				newTd += "<td onclick=\"location.href='/A/comBoard/content?P=1&No=" + newL.com_board_key + "'\"><div class='mainListTbDiv'>";
				if(newL.company_logo != null) newTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/"+ newL.company_logo +"'></div>";
				if(newL.company_logo == null) newTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/main_logo.png'></div>";
				
				
				
				
				
				newTd += "</div></td>";
			}); 
			
			if(newEmpty > 0) for(var i=0;i<newEmpty;i++) newTd += "<td><div class='mainListTbDiv'>비어있을 경우</div></td>";
			newTd += "</div></td>";
			
			$('#mainJobNewListTb').append(newTd +"</tr>");
		
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}