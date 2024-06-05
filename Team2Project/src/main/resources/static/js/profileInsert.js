$(document).ready(function(){
	
	// 카테고리
	var category_num = '1';
	var search_major_index = '0'

	$(document).on('click', '#category_add_button', function(event){  
		if($("#profile_group1").val() != "" && $("#profile_group2").val() != ""){ 
			alert('더이상 추가 불가능');
		}else{
			category_num = '2';
		}
	});
	//삭제
	$(document).on('click', '#x1', function(event){  
		category_num = '1';
		xBtAction(1);
	});
	$(document).on('click', '#x2', function(event){
		if($("#profile_group1").val() == ""){ 
			category_num = '1';
		}else{
			category_num = '2';
		}
		xBtAction(2);
	});
	//값 다 비우기
	function xBtAction(btNum) {
		$("#subCategoriesTable").empty();
       	$("#stepCategoriesTable").empty();
		$("#profile_group"+btNum).val(""); 
    	$("#profile_sub"+btNum).val("");
       	$("#profile_step"+btNum).val("");
       	$("#group"+btNum).empty();
       	$("#sub"+btNum).empty();
       	$("#step"+btNum).empty();
       	$("#x"+btNum).empty();
	}
	
	//대분류를 클릭하였을 때
	$(document).on('click', '.category_link', function(event){  
		event.preventDefault();  
		var categoryCode = $(this).data("code"); 
		var categoryName = $(this).text(); 
		$("#profile_group"+category_num).attr("size",categoryName.length);
		$("#profile_group"+category_num).val(categoryName); 
		$("#group"+category_num).text(categoryName); 
       	$("#x"+category_num).text("X"); 
		loadSubCategories(categoryCode);  
	});

	// 카테고리 중분류 클릭하였을 때
		$(document).on('click', '.subcategory_link', function(event){
		event.preventDefault();
		var subCategoryCode = $(this).data("code"); 
		var subCategoryName = $(this).text(); 
		$("#profile_sub"+category_num).attr("size",subCategoryName.length);
		$("#profile_sub"+category_num).val(subCategoryName);
		$("#sub"+category_num).text(">"+subCategoryName); 
		loadSteps(subCategoryCode); 
	});
    
	//카테고리 소분류 클릭하였을 때
	$(document).on('click', '.step_link', function(event){
		event.preventDefault();
		var stepCode = $(this).data("code");
		var stepName = $(this).text(); 
		
		var selectedStep = $("#profile_step"+1).val();
		
	    if(selectedStep != stepName) {
	        $("#profile_step"+category_num).attr("size",stepName.length);
	        $("#profile_step"+category_num).val(stepName);
	        $("#step"+category_num).text(">"+stepName); 
	        
	    } else {
	        alert("이미 선택된 소분류입니다.");
	        
	        var selectedSubCategory = $("#profile_sub"+category_num).val();
	        
	        if(selectedSubCategory) {
	            $("#sub"+category_num).text(">"+selectedSubCategory); 
	        } else {
	            $("#sub"+category_num).empty(); 
	        }
	        return;
	      
	    }
	    
		$("#profile_step"+category_num).attr("size",stepName.length);
		$("#profile_step"+category_num).val(stepName);// 선택된 소분류 코드를 숨겨진 필드에 저장
		$("#step"+category_num).text(">"+stepName); 
	});

	
	//대분류에서 선택한 코드 서버로 보내기
    function loadSubCategories(categoryCode) {  
        $.ajax({  
	        type: 'POST',
            url: "/com_board_group", 
            data: {"no": categoryCode}, //대분류 코드
            dataType: "json",
            success: function(response){
            	$("#subCategoriesTable").empty();//중분류 id (중분류 초기화)
            	$("#profile_sub"+category_num).val("");
               	$("#profile_step"+category_num).val("");
               	$("#sub"+category_num).empty();
               	$("#step"+category_num).empty();
                $.each(response, function(index, subCategory){ 
                    var row = "<li><a href='#' class='subcategory_link' data-code='" + subCategory.code + "'>" + subCategory.name + "</a></li>";
                    $("#subCategoriesTable").append(row); 
                });
                $("#stepCategoriesTable").empty(); 
            },
            error: function(xhr, status, error){
                console.error("Error:", error);
            }
    	});
    } //중분류 코드 보내서 소분류 데이터 가져오기
    function loadSteps(subCategoryCode) {
        $.ajax({
	        type: 'POST',
            url: "/com_board_sub",
            data: {"no": subCategoryCode},
            dataType: "json",
            success: function(response){
               	$("#stepCategoriesTable").empty();
               	$("#profile_step"+category_num).val(""); 
               	$("#step"+category_num).empty();
                $.each(response, function(index, step){
                    var row = "<li><a href='#' class='step_link' data-code='" + step.code + "'>" + step.name + "</a></li>";
                    $("#stepCategoriesTable").append(row);
                });
            },
            error: function(xhr, status, error){
                console.error("Error:", error);
            }
        });
    }
 	// 카테고리
             

 
 	

	//학교분류에 따라 학교폼 가져오기
	var selectedOption = 0; //학교 구분 저장할 변수
	var edu_kind_select = `
    	<select class="edu_kind" name="eDtoList[0].edu_kind">
			<option value="0" selected>고등학교</option>
			<option value="1">대학(2,3년)</option>
			<option value="2">대학교(4년)</option>
			<option value="3">대학원</option>
		</select>
    `;
    //공통 된 부분 변수에 저장해 놓기
	var edu_name_text = `<input name="eDtoList[0].edu_name" class="edu_name" placeholder="학교명">`;
	var edu_end_date_text = `<input type="date" name="eDtoList[0].edu_end_date" placeholder="졸업년도">`;
	var edu_status_text = `<input name="eDtoList[0].edu_status" placeholder="졸업상태">`;
	var edu_start_date_text = `<input type="date" name="eDtoList[0].edu_start_date" placeholder="입학년월">`;
	var edu_major_text = `<input name="eDtoList[0].edu_major" class="edu_major" placeholder="전공 및 문과/이과">`;
	
	var edu_remove_bt = `<button class="removeForm">삭제</button>`;
	//학력 추가 버튼 클릭	
	$(document).on('click', '#addEducation', function(event){  
        $("#add_edu_input_form").append("<div class='edu_input_form' id='edu_input_form'>"+edu_kind_select 
        		+ edu_name_text +edu_start_date_text + edu_end_date_text + edu_status_text+edu_major_text+edu_remove_bt
        		+"</div>");
	});
	
    // 삭제 버튼 클릭 시 해당 폼 제거
    $(document).on("click", ".removeForm", function () {
        $(this).closest(".edu_input_form").remove();
    });
    
	$(document).on('change', '.edu_kind', function(event){  
		//var row;
	    selectedOption = $(this).val();
	    $(this).closest(".edu_input_form").attr("class","here"); 
	    // 현재 변경된 '.edu_kind' 요소를 포함하는 가장 가까운 '.edu_input_form' 요소를 찾고, 
	    // 해당 요소의 클래스 이름을 'here'로 변경함(어떤 .edu_kind 요소가 변경되었는지를 식별하기 위해)
	    // * 종류를 바꾸면 모든 .edu_kind가 다 바뀌기 때문에 지정
	    $(this).closest(".here").empty(); 
	    $('#search_school').empty(); 
	    $('#search_major').empty(); 
	    if (selectedOption === "0") {
	        row = `
	        	<select class="edu_kind" name="eDtoList[0].edu_kind">
					<option value="0" selected>고등학교</option>
					<option value="1">대학(2,3년)</option>
					<option value="2">대학교(4년)</option>
					<option value="3">대학원</option>
				</select>
		    `;
	    	
	    	row += edu_name_text + edu_end_date_text + edu_status_text+edu_remove_bt;
	    } else if (selectedOption === "1") {
	        row = `
	        	<select class="edu_kind" name="eDtoList[0].edu_kind">
					<option value="0">고등학교</option>
					<option value="1" selected>대학(2,3년)</option>
					<option value="2">대학교(4년)</option>
					<option value="3">대학원</option>
				</select>
		    `;
	    	
	    	row += edu_name_text + edu_start_date_text + edu_end_date_text + edu_status_text + edu_major_text +edu_remove_bt;
	    } else if (selectedOption === "2") {   
	    	row = `
	        	<select class="edu_kind" name="eDtoList[0].edu_kind">
					<option value="0">고등학교</option>
					<option value="1">대학(2,3년)</option>
					<option value="2" selected>대학교(4년)</option>
					<option value="3">대학원</option>
				</select>
		    `;
			
			row += edu_name_text + edu_start_date_text + edu_end_date_text + edu_status_text + edu_major_text +edu_remove_bt;

	    } else if (selectedOption === "3") {
	    	row = `
	        	<select class="edu_kind" name="eDtoList[0].edu_kind">
					<option value="0">고등학교</option>
					<option value="1">대학(2,3년)</option>
					<option value="2">대학교(4년)</option>
					<option value="3" selected>대학원</option>
				</select>
		    `;
		    
	    	row += edu_name_text  + edu_start_date_text + edu_end_date_text + edu_status_text + edu_major_text +edu_remove_bt;
	    }
	    
	    $(".here").append(row);
	    $(".here").attr("class","edu_input_form"); 
	    //다시 원래 class로 복원(임시 클래스는 다음 변경을 위한 것이 아니기 때문에, 다음 변경을 위해 클래스를 다시 원래대로)
	});
    

    // 학교명 입력 폼에 입력할 때마다 학교명 검색
    // 가끔 검색 안됨
	$(document).on('input', '.edu_name', function(){
		var schoolName = $(this).val();
		var nowNum = $(this).parent("div").index();
		// 이 입력 필드가 속한 <div> 요소의 인덱스
		if(schoolName.length >= 2){ 
			searchSchool(schoolName,selectedOption,nowNum);
		}
	}); 
	   
   function searchSchool(schoolName,selectedOption,nowNum){
        $.ajax({
            type: 'POST',
            url: "/search_school_by_name",
            data: { "name": schoolName,"code":selectedOption},
            success: function(response){
                $("#search_school").empty();
                $.each(response, function(index, step){
                    var row = "<li><a href='#' data-index='" + nowNum + "' data-code='" + step.code + "'>" + step.name + "</a></li>";
                    $("#search_school").append(row);
                });
            },
            error: function(xhr, status, error){
                console.error("Error:", error);
            }
        });
    } 
 	//선택한 학교 텍스트창에 학교명 저장
	var selectedSchoolCode = ""; // 전역 변수로 학교 코드를 저장할 변수
	
	$(document).on('click', '#search_school a[data-code]', function(event){ 
		event.preventDefault();
		selectedSchoolCode = $(this).data("code");  
		var schoolName = $(this).text();  
		$(".edu_input_form").eq($(this).data("index")).find('.edu_name').val(schoolName);
	}); //$(this)의 data 속성에 지정된 인덱스
		
		
	//학과명 입력 폼에 입력할 때마다 학교명 검색
	$(document).on('input', '.edu_major', function(){
		 var majorName = $(this).val();
		 edu_major_index = $('#add_edu_input_form .edu_major').index($(this));
		 if(majorName.length >= 2 && selectedSchoolCode !== ""){ 
		   loadDepartments(selectedSchoolCode, majorName); 
		 }
	});  
		
		function loadDepartments(selectedSchoolCode, majorName) {
		    $.ajax({
		        type: 'POST',
		        url: "/get_department",
		        data: { "code": selectedSchoolCode, "name": majorName },
		        dataType: "json",
		        success: function(response) {
		            	$("#search_school").empty();
		            	$("#search_major").empty();
		            $.each(response, function(index, step){
	                    var row = "<li><a href='#' data-code='" + step.code + "'>" + step.name + "</a></li>";
	                    $("#search_major").append(row);
	                });
		            
	            },
		        error: function(xhr, status, error){
		            console.error("Error:", error);
		        }
		    });
		}
	
	//학과 선택 시 텍스트에 학과 저장 
	$(document).on('click', '#search_major a[data-code]', function(event){
	    event.preventDefault();
	    var department = $(this).text();
		$(".edu_input_form .edu_major").eq(edu_major_index).val(department);
		
	});
		
	
	
	
	// 경력
	var career_input = `
		<div class="career_input_form">
		<ul>
			<li>
			<input name="crDtoList[0].career_company" placeholder="회사명">
			<input type="date"  name="crDtoList[0].career_start_date" placeholder="입사년월">
			<input type="date" name="crDtoList[0].career_end_date" placeholder="퇴사년월">
			</li>
		</ul>
		<ul>
			<li>
			<input name="crDtoList[0].career_position" placeholder="직책">
			<input name="crDtoList[0].career_bye" placeholder="퇴사사유">
			</li>
		</ul>
		<textarea rows="15" cols="80" placeholder="담당업무" name="crDtoList[0].career_cont"></textarea>
		<button class="removeForm">삭제</button></div>
	    `;
	    
		$(document).on('click', '#addCareer', function(event){  
	        $("#add_career_input_form").append(career_input);
		});
	
    // 삭제 버튼 클릭 시 해당 폼 제거
    $(document).on("click", ".removeForm", function () {
        $(this).closest(".career_input_form").remove();
    });
    
	// 경력
	
		
	 // 자격증 리스트 불러오기
	var license_input = `
		<div class="license_input_form">
		<ul>
			<li>
			<input name="lDtoList[0].license_name" class="license_name" placeholder="자격증명">
			<input name="lDtoList[0].license_barhang" class="license_barhang" placeholder="발행처">
			<input type="date" name="lDtoList[0].license_date" placeholder="취득년월">
			</li>
		</ul>
		<button class="removeForm">삭제</button></div>
	    `;
	 

	$(document).on('click', '#addLicense', function(event){  
        $("#add_license_input_form").append(license_input);
	});

	  // 삭제 버튼 클릭 시 해당 폼 제거
    $(document).on("click", ".removeForm", function () {
        $(this).closest(".license_input_form").remove();
    });
	
    $(document).on('input', '.license_name', function(){
	    var certificationName = $(this).val();
		var nowNum = $(this).parent().parent().parent().index();
		//세 번의 부모 요소를 거슬러 올라가면 자격증 입력 폼을 감싸는 div 요소에 위치
	    if (certificationName.length >= 2) { 
	        searchCertifications(certificationName,nowNum);
	    }
	});
	function searchCertifications(certificationName,nowNum) {
	    $.ajax({
	        type: 'POST',
	        url: '/search_certifications', 
	        data: { "license_name": certificationName }, 
	        success: function(response) {
	        	$("#search_license").empty();
	        	$.each(response, function(index, step){
	                var row = "<li><a href='#' data-index='" + nowNum + "' data-code='" + step.license_barhang + "'>" + step.license_name + "</a></li>";
	                $("#search_license").append(row);
	            });
	        },
	        error: function(xhr, status, error){
	            console.error("Error:", error);
	        }
	    });
	} 
	
	//자격증 클릭하면 텍스트 창에 넣기
	$(document).on('click', '#search_license a[data-code]', function(event){
	    event.preventDefault();
	    var license = $(this).text();
	    var licensebarhang = $(this).data('code');
		$(".license_input_form").eq($(this).data("index")).find('.license_name').val(license);
		$(".license_input_form").eq($(this).data("index")).find('.license_barhang').val(licensebarhang);
		
	});
	
	
	  $("input[name=profile_image_input").on("change", function(){
		    let maxSize = 500 * 1024; 
			let fileSize = this.files[0].size; 

		    if(fileSize > maxSize){
				alert("파일첨부 사이즈는 500 * 1024 이내로 가능합니다.");
				$(this).val(''); 
				return; 
			}
	  });
	  
	  //이미지 보이게
	  $("#imageFile").on("change", function(event) {

		    var file = event.target.files[0];

		    var reader = new FileReader(); 
		    reader.onload = function(e) {

		        $("#preview").attr("src", e.target.result);
		    }

		    reader.readAsDataURL(file);
		});
	  
	// 확장자가 이미지 파일인지 확인
	  function isImageFile(file) {

	      var ext = file.name.split(".").pop().toLowerCase(); // 파일명에서 확장자를 가져온다. 

	      return ($.inArray(ext, ["jpg", "jpeg", "gif", "png"]) === -1) ? false : true;
	  }
	
	  function isOverSize(file) {

		  var maxSize = 3 * 1024 * 1024; // 3MB로 제한 

		    return (file.size > maxSize) ? true : false;
		}
	 
});
