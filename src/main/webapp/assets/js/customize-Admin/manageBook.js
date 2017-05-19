function submitClear() {
	$("#name").val("");
	$("#categoryId").val("0");
}

//click button search
$("#btn_seach").click(function(e) {

	$('#messageContainer').html('');
	e.preventDefault();
	var postData = $(this).closest('form').serializeArray();

	$.ajax({
		url : "/SpringSecurity/managementBook/search",
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
                            "mDataProp" : "bookCode",
                            "mRender" : function(data, type, row) {
                            	return "<button onclick='clickBtnDel("+row.bookId+",this)'>Delete</button>"
                                +"&nbsp;&nbsp;&nbsp;<a target='_blank' href='/SpringSecurity/managementBook/detail/" + row.bookId + "'>"
                                        + data+"</a>";
                            },
                        }, { "mDataProp" : "name"
                        }, { "mDataProp" : "categoriesName"
                        }, { "mDataProp" : "publishersName"
                        }, { "mDataProp" : "statusBook"
                        }, { "mDataProp" : "numberBook"
                        }, { "mDataProp" : "numberBorrowed"
                        
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


//Donwload csv
$("#btn_download").click(function() {
	
	$("#btn_seach").click();
	$("#searchForm").attr("action", "/SpringSecurity/managementBook/downloadCSV");
	$("#searchForm").submit();
	
});


//Delete on list
function clickBtnDel(idUser, el) {
	if (confirm("Are you sure delete record?") == true) {
		$('#messageContainer').html('');
		
		var formURL = "/SpringSecurity/managementBook/delete/" + idUser;
		$.ajax({
			url : formURL,
			type : "GET",
			data : false,
			dataType : 'json',
			success : function(data) {
				if (data == 'success') {
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