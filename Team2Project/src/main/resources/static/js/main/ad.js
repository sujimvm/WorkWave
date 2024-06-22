$(document).ready(function() {
	showSlides();
});

var slideIndex = 0;

function showSlides() {
    var i;
    var slides = document.getElementsByClassName("mainAd");
    var dots = document.getElementsByClassName("dot");
    for (i = 0; i < slides.length; i++) {
       slides[i].style.display = "none";  
    }
    slideIndex++;
    if (slideIndex > slides.length) {slideIndex = 1}    
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    slides[slideIndex-1].style.display = "block";  
    dots[slideIndex-1].className += " active";
    setTimeout(showSlides, 3000); // Change image every 2 seconds
}

// 특정 슬라이드를 보여주는 함수
function currentSlide(n) {
    showSpecificSlide(slideIndex = n);
}

// 특정 슬라이드를 보여주는 함수
function showSpecificSlide(n) {
    var i;
    var slides = document.getElementsByClassName("mainAd");
    var dots = document.getElementsByClassName("dot");
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";  
    }
    if (n > slides.length) {n = 1}
    if (n < 1) {n = slides.length}
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    slides[n-1].style.display = "block";  
    dots[n-1].className += " active";
}

document.addEventListener("DOMContentLoaded", function(){
	var ad1 = document.getElementById('ad1')
	var ad2 = document.getElementById('ad2')
	var ad3 = document.getElementById('ad3')
	
	ad1.addEventListener('click', function() {
    	window.open('https://studyinjpn.com/ko'); // 이동할 URL로 수정
    });
    
    ad2.addEventListener('click', function() {
    	window.open('http://www.eduplusshow.co.kr/'); // 이동할 URL로 수정
    });
    
    ad3.addEventListener('click', function() {
    	window.open('https://www.startup-afro.kr/fairDash.do'); // 이동할 URL로 수정
    });
})