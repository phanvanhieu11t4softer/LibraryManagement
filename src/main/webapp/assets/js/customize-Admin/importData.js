$(document).on('click', '.browse', function() {
	var file = $(this).parent().parent().parent().find('.file');
	file.trigger('click');
});

$(document).on(
		'change',
		'.file',
		function() {
			$(this).parent().find('.form-control').val(
					$(this).val().replace(/C:\\fakepath\\/i, ''));
		});

function goBack() {
	window.history.back();
}

function checkEmpty(str) {
	if (str == "") {
		return true;
	}
	return false;
}
$('#btnImport').on("click", function(e) {
	e.preventDefault();
	var nameTable = $("#nameTable").val();
	var fileImport = $("#fileImport").val();
	var fileName = $("#fileName").val();
	var check = true;
	$("#err_nameTable").text("");
	$("#err_file").text("");
	$("#err_formatFile").text("");
	$("#err_data").text("");
	$("#error_table").text("");
	$("#error_file").text("");
	$("#success").text("");
	$("#fail").text("");
	
	if (checkEmpty(nameTable)) {
		check = false;
		$("#err_nameTable").text("Please choose Table name");
	} else {
		$("#err_nameTable").text("");
	}

	if (checkEmpty(fileImport)) {
		check = false;
		$("#err_file").text("Please choose File");
	} else {
		var suffixesFile = fileName.substring(fileName.lastIndexOf(".") + 1);
		$("#err_file").text("");
		if (!("csv" == suffixesFile)) {
			check = false;
			$("#err_formatFile").text("Please choose File CSV");
		}else{
			$("#err_formatFile").text("");
		}
	}
	if (check) {
		$("#formImport").submit();
	}
});
