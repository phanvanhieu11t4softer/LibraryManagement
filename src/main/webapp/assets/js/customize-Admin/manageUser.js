function submitClear() {
	    $("#txtName").val("");
	    $('#txtPermission').val("0");
}
$("#btn_seach").click(function(e) {

    $('#messageContainer').html('');
    e.preventDefault();
    var postData = $(this).closest('form').serializeArray();

    $.ajax({
            url : "/SpringSecurity/managementUsers/search",
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
                    $('#resultSearch').removeClass(
                            'hidden_elem');
                    $('#resultSearch').addClass(
                            'clearfix body manageUser');

                    $('#dataTables-result').DataTable({
                        "bProcessing" : true,
                        "aaData" : data,
                        "createdRow" : function(row, data, dataIndex) {
                            $(row).addClass('gradeX');
                        },
                        "aoColumns" : [
                            {
                                "mDataProp" : "userName",
                                "mRender" : function(data, type, row) {
                                	var a = "aasa";
                                    return "<button onclick='clickBtnDel("+row.userId+",this)'>Del</button>"
                                    +"&nbsp;&nbsp;&nbsp;<a target='_blank' href='/SpringSecurity/managementUsers/detail/" + row.userId + "'>"
                                            + data+"</a>";
                                },
                            }, { "mDataProp" : "name"
                            }, { "mDataProp" : "birthDate"
                            }, { "mDataProp" : "address"
                            }, {"mDataProp" : "email"
                            }, { "mDataProp" : "sex"
                            }, { "mDataProp" : "phone"
                            }, { "mDataProp" : "permissions.permissionName"
                            }],
                        responsive : true
                    });
                } else {
                    $('#resultSearch').addClass('hidden_elem');
                    $('#messageContainer').html($("#mgsNoResult").text());
                }
            },
            error : function(xhr, status, error) {
                $('#resultSearch').addClass(
                        'hidden_elem');
                $('#messageContainer').html($("#mgsNoResult").text());
            }
        });
    

});
//Detail user
$(".birthday_picker").datepicker({
	dateFormat : 'yy-mm-dd',
	minDate : new Date('1920-01-01'),
	maxDate : '-1d',
	onSelect: function(dateText) {
		if (dateText != $('#lblBirthDate').text()) {
			$("#savebtn").prop('disabled', false);
		}
	}
});

function clickBtnEdit() {
	$(".editForm").removeClass('hidden_elem');
	$(".lableForm").addClass('hidden_elem');
	$("#savebtn").prop('disabled', true);
}

function clickBtnCancel() {
	$(".lableForm").removeClass('hidden_elem');
	$(".editForm").addClass('hidden_elem');
	$("#updateForm")[0].reset();
}

// Del user on list
function clickBtnDel(idUser, el) {
	if (confirm("Are you sure delete record?") == true) {
		$('#messageContainer').html('');
		
		var formURL = "/SpringSecurity/managementUsers/delete/" + idUser;
		$.ajax({
			url : formURL,
			type : "GET",
			data : false,
			dataType : 'json',
			success : function(data) {
				if (data == 1) {
					// remove datatable
					var a = $('#dataTables-result').DataTable();
					a.row($(el).parents('tr')).remove().draw();
					
					// Message
					$('#messageContainer').html($("#mgsSuccess").text());
				}
				else {
					$('#messageContainer').html($("#mgsError").text());
				}
			},
			error : function(error) {
				$('#messageContainer').html($("#mgsError").text());
			}
		});
	}
};

// update user
$("#savebtn").click(function() {
	if ($("#updateForm").valid()) {
		$("#updateForm").submit();
	}
});

jQuery.extend(jQuery.validator.messages, {
	required : "This is item required.",
	email: "This is invalid email.",
	number: "This is invalid number"
});

//Setup form validation on the #update-form element
var validator = $("#updateForm").validate({
	 errorElement: 'div',
	// Specify the validation rules
	rules : {
		name : "required",
		address : "required",
		birthDate : "required",
		phone: 
		{  
			required: true,
			number: true
		},
		email : {  
		required: true,
		email: true
	},
	},
	errorPlacement : function(error, element) {
		if (element.is(":radio")) {
			error.appendTo(element.parents('.form-group'));
		} else { // This is the default behavior 
			error.insertAfter(element);
		}
	}
});

$("#name").blur(function() {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});

$("#birthDate").blur(function() {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});

$("#address").blur(function() {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});

$("#phone").blur(function() {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});

$("#email").blur(function() {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});

$( "select" ).change(function () {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});
$('input[type=radio][name=sex]').change(function () {
	if(checkItemChangeValue()) {
		$("#savebtn").prop('disabled', false);
	}
	else {
		$("#savebtn").prop('disabled', true);
	}
});

function checkItemChangeValue() {
	var name = $('#name').val();
	var lblName = $('#lblName').text();
	
	var permissionsName = $('#permissionsName option:selected').text();
	var lblPermissionsName = $('#lblPermissionsName').text();
	
	var birthDate = $('#birthDate').val();
	var lblBirthDate = $('#lblBirthDate').text();
	
	var address = $('#address').val();
	var lblAddress = $('#lblAddress').text();
	
	var phone = $('#phone').val();
	var lblPhone = $('#lblPhone').text();
	var sex = $('input[type=radio][name=sex]:checked').val();
	var lblSex = 1;
	if ("Fmale" == $('#lblSex').text()) {
		lblSex = 0;
	}
	
	var email = $('#email').val();
	var lblEmail = $('#lblEmail').text();
	if (name == lblName 
			&& permissionsName == lblPermissionsName
			&& birthDate == lblBirthDate
			&& address == lblAddress
			&& phone == lblPhone
			&& sex == lblSex
			&& email == lblEmail
			) {
		return false;
	}
	else{
		return true;
	}
}

$("#btn_download").click(function() {
	
	$("#btn_seach").click();
	$("#searchForm").attr("action", "/SpringSecurity/managementUsers/downloadCSV");
	$("#searchForm").submit();
	
});