/* 
 * Initialize
*/
$(document).ready(function() {
	if($(".errorMessage").length) {
		$("input[name=fileImportFileName]").val("");
	}
});

/* 
 * Click update load file CSV
*/
$(document).on('click', '.browse', function() {
	var file = $("#fileImport");
	file.trigger('click');
});

/*
 * Change file CSV
 */
$(document).on('change', '.file', function() {
	$(this).parent().find('.form-control').val(
	$(this).val().replace(/C:\\fakepath\\/i, ''));
	$(".actionMessage").remove();
	$(".errorMessage").remove();
	$(".errorMessageInput").remove();
});

/*
 * Event click button import file
 */
$(document).on('click', '#btnImport', function() {
	$(".actionMessage").remove();
	$(".errorMessage").remove();
	$(".errorMessageInput").remove();
	$("#frmImport").submit();
});
