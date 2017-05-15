var formatDate = 'yy-mm-dd';
var minDate = new Date('2010-01-01');
var maxDate = '-0d';

$("#txtIntenDateBor").datepicker({
	dateFormat : formatDate,
	minDate : minDate,
	maxDate : maxDate,
	onSelect: function(dateText) {
		if ($("#txtIntenDatePay").val() != "" ) {
			if (new Date(dateText) > new Date($("#txtIntenDatePay").val())) {
				$("#btn_seach").prop('disabled', true);
				$("#btn_download").prop('disabled', true);
			} else {
				$("#btn_seach").prop('disabled', false);
				$("#btn_download").prop('disabled', false);
			}
		}
	}
});

$("#txtIntenDatePay").datepicker({
	dateFormat : formatDate,
	minDate : minDate,
	onSelect: function(dateText) {
		if ($("#txtIntenDateBor").val() != "" ) {
			if (new Date(dateText) < new Date($("#txtIntenDateBor").val())) {
				$("#btn_seach").prop('disabled', true);
				$("#btn_download").prop('disabled', true);
			} else {
				$("#btn_seach").prop('disabled', false);
				$("#btn_download").prop('disabled', false);
			}
		}
	}
});

$("#txtDateBor").datepicker({
	dateFormat : formatDate,
	minDate : minDate,
	maxDate : maxDate,
	onSelect: function(dateText) {
		if ($("#txtDatePay").val() != "" ) {
			if (new Date(dateText) > new Date($("#txtDatePay").val())) {
				$("#btn_seach").prop('disabled', true);
				$("#btn_download").prop('disabled', true);
			} else {
				$("#btn_seach").prop('disabled', false);
				$("#btn_download").prop('disabled', false);
			}
		}
	}
});

$("#txtDatePay").datepicker({
	dateFormat : formatDate,
	minDate : minDate,
	maxDate : maxDate,
	onSelect: function(dateText) {
		if ($("#txtDateBor").val() != "" ) {
			if (new Date(dateText) < new Date($("#txtDateBor").val())) {
				$("#btn_seach").prop('disabled', true);
				$("#btn_download").prop('disabled', true);
			} else {
				$("#btn_seach").prop('disabled', false);
				$("#btn_download").prop('disabled', false);
			}
		}
	}
});

// click button search
$("#btn_seach").click(function(e) {

	$('#messageContainer').html('');
	e.preventDefault();
	var postData = $(this).closest('form').serializeArray();

	$.ajax({
		url : "/SpringSecurity/managementBorrowed/search",
		type : "POST",
		data : postData,
		async : false,
        success : function(data, textStatus, jqXHR) {
            if ($.fn.DataTable
                    .isDataTable('#dataTables-result')) {
                $('#dataTables-result').DataTable()
                        .destroy();
            }

            if (data != "") {
                $('#dataTables-result').DataTable({
                    "bProcessing" : true,
                    "aaData" : data,
                    "createdRow" : function(row, data, dataIndex) {
                        $(row).addClass('gradeX');
                    },
                    "aoColumns" : [
                        {
                            "mDataProp" : "borrowedCode",
                            "mRender" : function(data, type, row) {
                            	var a = "aasa";
                                return "<a target='_blank' href='/SpringSecurity/managementBorrowed/detail/" + row.borrowedId + "'>"
                                        + data+"</a>";
                            },
                        }, { "mDataProp" : "userInfo.userName"
                        }, { "mDataProp" : "dIntendBorrowed"
                        }, { "mDataProp" : "dIntendArrived"
                        }, { "mDataProp" : "dateBorrrowed"
                        }, { "mDataProp" : "dateArrived"
                        }, { "mDataProp" : "status"
                        
                        }],
                    responsive : true
                });
                
                $('#resultSearch').removeClass('hidden_elem');
            	$('#resultSearch').addClass('clearfix body manageUser');
            } else {
            	$('#resultSearch').addClass('hidden_elem');
            	 $('#messageContainer').html($("#mgsNoResult").text());
            }
        },
        error : function(xhr, status, error) {
        	 $('#resultSearch').addClass('hidden_elem');
        	 $('#messageContainer').html($("#mgsNoResult").text());
        }
    });
});

function submitClear() {
    $("#txtBorrowedCode").val("");
    $("#txtStatus").val("0");
    $("#txtIntenDateBor").val("");
    $("#txtIntenDatePay").val("");
    $("#txtDateBor").val("");
    $("#txtDatePay").val("");
    $("#btn_seach").prop('disabled', false);
	$("#btn_download").prop('disabled', false);
}

// clear data of textbox date
$("#txtIntenDateBor").change(function() {
	if ($("#txtIntenDateBor").val() == '') {
		$("#btn_seach").prop('disabled', false);
		$("#btn_download").prop('disabled', false);
	}
});
$("#txtIntenDatePay").change(function() {
	if ($("#txtIntenDatePay").val() == '') {
		$("#btn_seach").prop('disabled', false);
		$("#btn_download").prop('disabled', false);
	}
});
$("#txtDateBor").change(function() {
	if ($("#txtDateBor").val() == '') {
		$("#btn_seach").prop('disabled', false);
		$("#btn_download").prop('disabled', false);
	}
});
$("#txtDatePay").change(function() {
	if ($("#txtDatePay").val() == '') {
		$("#btn_seach").prop('disabled', false);
		$("#btn_download").prop('disabled', false);
	}
});

// download CSV
$("#btn_download").click(function() {
	
	$("#btn_seach").click();
	$("#searchForm").attr("action", "/SpringSecurity/managementBorrowed/downloadCSV");
	$("#searchForm").submit();
	
});

// update
var status_wait = '1';
var status_approve = '2';
var status_cancel = '3';
var status_borrowed = '4';
var status_finish = '5';

function clickBtnEdit() {
	$("#editbtn").addClass('hidden_elem');
	$("#savebtn").removeClass('hidden_elem');
	$("#cancelbtn").removeClass('hidden_elem');
	$("#status").prop('disabled', false);
	$("#dataTables-result").find("select").prop('disabled', false);
	$('#messageContainer').html("");
}

function clickBtnCancel() {
	$("#editbtn").removeClass('hidden_elem');
	$("#savebtn").addClass('hidden_elem');
	$("#savebtn").prop('disabled', true);
	$("#cancelbtn").addClass('hidden_elem');
	$("#status").prop('disabled', true);
	$("#dateBorrrowed").prop('disabled', true);
	$("#dateArrived").prop('disabled', true);
	$("#dataTables-result").find("select").prop('disabled', true);
	$("#updateForm")[0].reset();
	$('#messageContainer').html("");
}
$("#status" ).change(function () {
	if ($("#status option:selected").val() == status_approve) {
		$("#dateBorrrowed").prop('disabled', true);
		$("#dateArrived").prop('disabled', true);
		$("#dateBorrrowed").val("");
		$("#dateArrived").val("");
	} else if ($("#status option:selected").val() == status_cancel) {
		$("#dateBorrrowed").prop('disabled', true);
		$("#dateArrived").prop('disabled', true);
		$("#dateBorrrowed").val("");
		$("#dateArrived").val("");
		$("#savebtn").prop('disabled', false);
	}
	else if ($("#status option:selected").val() == status_borrowed) {
		$("#dateBorrrowed").prop('disabled', false);
		$("#dateArrived").prop('disabled', true);
		$("#dateArrived").val("");
	}
	else {
		$("#dateBorrrowed").prop('disabled', false);
		$("#dateArrived").prop('disabled', false);
	}

	var defaultData = $("#status").attr("data");
	var changeData = $("#status option:selected").text();

	if (defaultData != "Request" && defaultData != changeData) {
		$("#savebtn").prop('disabled', false);
	}
	else if(defaultData == "Request"  && defaultData != changeData) {

		var flagChange = true;
		$("#dataTables-result").find("select").each(function(){
			if ($(this).find("option:selected").val() == status_wait) {
				flagChange = false;
				return;
			}
		})

		if (flagChange) {
			$("#savebtn").prop('disabled', false);
		}
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
})

$("#dataTables-result").find("select").change(function () {
	if ($("#status option:selected").val() != status_cancel &&
			 $("#status option:selected").val() != status_approve) {

		var flgWait = true;
		$("#dataTables-result").find("select").each(function(){

			if ($(this).find("option:selected").val() == status_wait) {
				
				var flgWait = false;
				return;
			}
		})

		if (flgWait && $("#status option:selected").val() != status_wait) {
			$("#savebtn").prop('disabled', false);
		} else {
			$("#savebtn").prop('disabled', true);
		}

	} else if ($("#status option:selected").val() == status_approve) {
		var flgApprove = false;
		$("#dataTables-result").find("select").each(function(){
			if ($(this).find("option:selected").text() != $(this).attr("data")) {
				flgApprove = true;
				return;
			}
		})

		if (flgApprove) {
			$("#savebtn").prop('disabled', false);
		} else {
			$("#savebtn").prop('disabled', true);
		}
	}
	else {
		$("#savebtn").prop('disabled', true);
	}

})

// check date borrowed and date arrived
var difDate;

// update - Datepicker
$("#dateBorrrowed").datepicker({
	dateFormat : formatDate,
	minDate : minDate,
	maxDate : maxDate,
	onSelect: function(dateText) {
		$("#dateArrived").datepicker( "option", "minDate", dateText || '0');
		var start = $("#dateBorrrowed").datepicker("getDate");
		var end = $("#dateArrived").datepicker("getDate");
		var days = (end - start) / (1000 * 60 * 60 * 24);
		difDate = days;
	}
	
});

$("#dateBorrrowed").change(function () {
	if ($("#dateBorrrowed").val() == '') {
		$("#dateArrived").datepicker( "option", "minDate", minDate);
	}
	var start = $("#dateBorrrowed").datepicker("getDate");
	var end = $("#dateArrived").datepicker("getDate");
	var days = (end - start) / (1000 * 60 * 60 * 24);
	difDate = days;
})

$("#dateArrived").change(function () {
	var start = $("#dateBorrrowed").datepicker("getDate");
	var end = $("#dateArrived").datepicker("getDate");
	var days = (end - start) / (1000 * 60 * 60 * 24);
	difDate = days;
})
$("#dateArrived").datepicker({
	dateFormat : formatDate,
	minDate : $("#dateBorrrowed").val(),
	maxDate : maxDate,
	onSelect : function(dateText) {
		var start = $("#dateBorrrowed").datepicker("getDate");
		var end = $("#dateArrived").datepicker("getDate");
		var days = (end - start) / (1000 * 60 * 60 * 24);
		difDate = days;
	}
});

$("#savebtn").click(function() {

	var flagWait = false;
	
	$("#dataTables-result").find("select").each(function(){
		if ($(this).find("option:selected").val() == status_wait) {
			flagWait = true

			return;
		}
	})

	if (flagWait) {
		// Change status of borrowed detail = cancel if status of borrowed = cancel
		if ($("#status option:selected").val() == status_cancel) {
			$("#dataTables-result").find("select").val(status_cancel);
			$("#updateForm").submit();
		} else {
			$('#messageContainer').html($("#mgsCheckUpd").text());
			$("#savebtn").prop('disabled', true);
		}

	} else {

		if ($("#status").attr("data") == "Request"
			|| $("#status").attr("data") == "Approve") {
			var flagCancel = true;
	
			// change status borrowed = cancel if find one status of borrowed = accept
			$("#dataTables-result").find("select").each(function(){
				if ($(this).find("option:selected").val() != status_cancel) {
					flagCancel = false;
					return;
				}
			})
			if (flagCancel) {
				$("#status").val('3');
				$("#updateForm").submit();
			}
		}

		// Check invalid data
		var defaultData = $("#status").attr("data");
		
		if(defaultData == "Request" && 
				$("#status option:selected").val()== status_finish) {
			if ($("#dateBorrrowed").val() == '' || $("#dateArrived").val() == '' || difDate < 0) {
				$('#messageContainer').html($("#mgsCheckUpd").text());
			}
			else {
				$("#updateForm").submit();
			}
		}
		else if ($("#status option:selected").val()== status_borrowed) {
			if ($("#dateBorrrowed").val() == '') {
				$('#messageContainer').html($("#mgsCheckUpd").text());
			}
			else{
				$("#updateForm").submit();
			}
		}
		else if ($("#status option:selected").val()== status_finish) {
			if ($("#dateArrived").val() == '') {
				$('#messageContainer').html($("#mgsCheckUpd").text());
			}else {
				$("#updateForm").submit();
			}
		}
		else {
			$("#updateForm").submit();
		}
	}
})
