// Initialize slick carousel, accordion, and DataTable
jQuery(function($) {
    $(".carousel").slick({
        slidesToShow: 1,
        prevArrow: '<div style="font-size: 25px" class="slick-prev fa fa-chevron-left"></div>',
        nextArrow: '<div style="font-size: 25px" class="slick-next fa fa-chevron-right"></div>',
    });
    $("#accordion" ).accordion({
        header: "> div > h3",
        collapsible: true,
        icons: null,
        heightStyle: "content",
        active: false,
        activate: function (event, ui) {
            if (!ui.newHeader.length) return;
            let scrollPosition = ui.newHeader.offset().top - $("#landmark-list").offset().top;
            $('#landmark-list').animate({
                scrollTop: '+=' + (scrollPosition - 15)
            }, 'fast');
        }
    });
    $('#edit-landmark-table').DataTable();
});