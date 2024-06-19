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
			
			var timeEmpty = 8 - time.length;
			var hotEmpty = 8 - hot.length;
			var newEmpty = 8 - newL.length;
			
			const today = new Date();
			
			/* 마감 공고 */
			time.forEach(function(time,index) {
				const endday = new Date(time.com_board_end_date);
				let diff = Math.ceil(Math.abs(endday.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

				if(index == 4) timeTd += "</tr><tr>";
				timeTd += "<td onclick=\"location.href='/A/comBoard/content?P=1&No=" + time.com_board_key + "'\"><div class='mainListTbDiv'>";
				if(time.company_logo != null) timeTd +=  "<div class='mainListLogoDiv'><img src='/image/logo/"+ time.company_logo +"'></div>";
				if(time.company_logo == null) timeTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/main_logo.png'></div>";
				timeTd +=  "<div class='mainListNameDiv'>"+ time.company_name +"</div>";
				timeTd +=  "<div class='mainListTitleDiv'>"+ time.com_board_title +"</div>";
				timeTd +=  "<div class='mainListDateDiv'><span>D-"+ diff +"</span></div>";
				timeTd += "</div></td>";
			}); 
			
			if(timeEmpty > 0) {
				for(var i=0;i<timeEmpty;i++){
					if(timeEmpty >= 4) if(i == timeEmpty-4) timeTd += "</tr><tr>";
					timeTd += "<td><div class='mainListTbDiv mainListNonDiv'>NO DATA</div></td>";
				}
			}
			
			timeTd += "</div></td>";
			
			$('#mainJobTimeListTb').append(timeTd +"</tr>");
				
				
				
				
			/* 인기 공고 */
			hot.forEach(function(hot,index) {
				const endday = new Date(hot.com_board_end_date);
				let diff = Math.ceil(Math.abs(endday.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

				if(index == 4) hotTd += "</tr><tr>";
				hotTd += "<td onclick=\"location.href='/A/comBoard/content?P=1&No=" + hot.com_board_key + "'\"><div class='mainListTbDiv'>";
				if(hot.company_logo != null) hotTd +=  "<div class='mainListLogoDiv'><img src='/image/logo/"+ hot.company_logo +"'></div>";
				if(hot.company_logo == null) hotTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/main_logo.png'></div>";
				hotTd +=  "<div class='mainListNameDiv'>"+ hot.company_name +"</div>";
				hotTd +=  "<div class='mainListTitleDiv'>"+ hot.com_board_title +"</div>";
				hotTd +=  "<div class='mainListDateDiv'><span>D-"+ diff +"</span></div>";
				hotTd += "</div></td>";
			}); 
			
			if(hotEmpty > 0) {
				for(var i=0;i<hotEmpty;i++){
					if(hotEmpty >= 4) if(i == hotEmpty-4) hotTd += "</tr><tr>";
					hotTd += "<td><div class='mainListTbDiv mainListNonDiv'>NO DATA</div></td>";
				}
			}
			
			hotTd += "</div></td>";
			
			$('#mainJobHotListTb').append(hotTd +"</tr>");
				
				
			/* 최신 공고 */
			newL.forEach(function(newL,index) {
				const endday = new Date(newL.com_board_end_date);
				let diff = Math.ceil(Math.abs(endday.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

				if(index == 4) newTd += "</tr><tr>";
				newTd += "<td onclick=\"location.href='/A/comBoard/content?P=1&No=" + newL.com_board_key + "'\"><div class='mainListTbDiv'>";
				if(newL.company_logo != null) newTd +=  "<div class='mainListLogoDiv'><img src='/image/logo/"+ newL.company_logo +"'></div>";
				if(newL.company_logo == null) newTd +=  "<div class='mainListLogoDiv'><img src='/image/icon/main_logo.png'></div>";
				newTd +=  "<div class='mainListNameDiv'>"+ newL.company_name +"</div>";
				newTd +=  "<div class='mainListTitleDiv'>"+ newL.com_board_title +"</div>";
				newTd +=  "<div class='mainListDateDiv'><span>D-"+ diff +"</span></div>";
				newTd += "</div></td>";
			}); 
			
			if(newEmpty > 0) {
				for(var i=0;i<newEmpty;i++){
					if(newEmpty >= 4) if(i == newEmpty-4) newTd += "</tr><tr>";
					newTd += "<td><div class='mainListTbDiv mainListNonDiv'>NO DATA</div></td>";
				}
			}
			
			newTd += "</div></td>";
			
			$('#mainJobNewListTb').append(newTd +"</tr>");
		
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}