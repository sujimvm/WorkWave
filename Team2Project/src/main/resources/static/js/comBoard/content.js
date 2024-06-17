// 화면 시작 
$(document).ready(function() {
	getRecommendList(1); // 추천 인재
	
});

// 공고리스트 조회 및 출력
function getRecommendList(page) {
	
	$.ajax({
		url: '/ajax/recommendList',
		type: 'post',
		dataType: 'json',
		data:{"page":page,"code":$('#code').val()},
		success: function(map) {
			$('#recommendList').empty(); // Clear any existing rows
			$('#pagination').empty();
			var list = map.list;
			var paging = map.paging;
			var paging_li = "";
			list.forEach(function(list) {
				$('#recommendList').append("<div>"+list+"</div>");
			}); 

			
			if(paging != undefined && paging.page > paging.block){
				paging_li += "<li><a href='javascript:getRecommendList(1)'>맨처음</a></li>";
			}
			if(paging != undefined && paging.page > paging.block){ 
				paging_li += "<li><a href='javascript:getRecommendList("+(paging.startBlock-1)+")'> ← </a></li>";
			}
			for(var i = paging.startBlock; i<= paging.endBlock; i++){ 
				paging_li += "<li><a href='javascript:getRecommendList("+i+")'>"+i+"</a></li>";
			}
			if(paging != undefined && paging.endBlock < paging.allPage){ 
				paging_li += "<li><a href='javascript:getRecommendList("+(paging.endBlock+1)+")'> → </a></li>";
			}
			if(paging != undefined && paging.endBlock < paging.allPage){ 
				paging_li += "<li><a href='javascript:getRecommendList("+paging.allPage+")'>맨뒤</a></li>";
			}
		
			$('#pagination').append(paging_li);
		
		},
		error: function(xhr, status, error) {
			console.error(xhr);
		}
	});
}

