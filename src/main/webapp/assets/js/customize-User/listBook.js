function submitClear() {
	    $("#txtName").val("");
	    $('#txtCategoryName').val("0");
}

$("#searchForm").submit(function(e) {

    $('#messageContainer').html('');

    var postData = $(this).serializeArray();
    var formURL = $(this).attr("action");

    $.ajax({
            url : formURL,
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
                                "mDataProp" : "nameBook",
                                "mRender" : function(data, type, row) {
                                	var a = "aasa";
                                    return "<button onclick='clickBtnDel("+row.bookId+",this)'>Borrow</button>"
                                    +"<a target='_blank' href='/LibraryManagementProject/managementBook/detail/" + row.bookId + "'>"
                                            + data+"</a>";
                                },
                            }, { "mDataProp" : "bookCode"
                            }, { "mDataProp" : "categoryName"
                            }, { "mDataProp" : "publisherName"
                            }, { "mDataProp" : "numberBook"
                            }, { "mDataProp" : "numberRest"
                            }, { "mDataProp" : "authorName"
                            }],
                        responsive : true
                    });
                } else {
                    $('#resultSearch').addClass('hidden_elem');
                    $('#messageContainer').html('<p>No search results found. Please input condition other</p>');
                }
            },
            error : function(xhr, status, error) {
                $('#resultSearch').addClass(
                        'hidden_elem');
                $('#messageContainer').html('<p>No search results found. Please input condition other</p>');
            }
        });
    e.preventDefault(); // STOP default action
});

//Del user on list
function clickBtnDel(bookId, el) {
	if (confirm("Are you sure borrow book?") == true) {
		$('#messageContainer').html('');
		
		var dateUpd = $("#"+bookId).text();
		
		var formURL = "/LibraryManagementProject/ordernow/" + bookId;
		$.ajax({
			url : formURL,
			type : "GET",
			data : {
				dateUpd : dateUpd
			},
			dataType : 'json',
			success : function(data) {
				if (data == 1) {
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

function popupCenter(url, title, w, h) {
	var left = (screen.width/2)-(w/2);
	var top = (screen.height/2)-(h/2);
	return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
	} 