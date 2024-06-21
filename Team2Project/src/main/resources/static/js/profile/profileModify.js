    // 삭제 버튼 클릭 이벤트 핸들러
      $(document).on("click", ".removeForm", function () {
          $(this).closest(".portfolio-item").remove();
      });
      
      // 프로필 이미지 변경
      $(function(){
    		$("#profile_image").on("change", function(event){
    			var file = event.target.files[0]
    			debugger;
    			
    			var maxSize = 500 * 1024;  

    		    if(file.size > maxSize){
    				alert("파일첨부 사이즈는 5MB 이내로 가능합니다."); 
    				$(this).val(''); 
    				return; 
    			}
    			
    			var reader = new FileReader();
    			debugger;
    			reader.onload = function(e){
    				$("#preview_profile").attr("src", e.target.result);
    			}
    			debugger;
    			reader.readAsDataURL(file);
    			debugger;
    			$("#preview_profile").show();
    			debugger;
    			$("#original_profile").hide();
    			debugger;
    					
    		});
    	});

      	//포폴 변경
     $(function(){
    $(".changeFile").click(function() {
        $("#profile_ppt").click();
    });

    $("#profile_ppt").on("change", function(event){
        var file = event.target.files[0];
        
        var maxSize = 10 * 1024 * 1024;  // 5MB
        
        if(file.size > maxSize){
            alert("파일 첨부 사이즈는 5MB 이내로 가능합니다.");
            $(this).val('');
            return;
        }
        
        var fileName = file.name;
        var fileSize = file.size;
        alert("파일 이름: " + fileName + ", 파일 크기: " + fileSize + " bytes");

        // 변경된 파일 이름을 표시
        $("#original_profile_ppt").text(fileName);
    });
});


