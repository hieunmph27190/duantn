function converArrayToObject(dataForm){
  if (Array.isArray(dataForm))
  var dataFormObject = {}
  for (const data of dataForm) {
    dataFormObject[data.name]=data.value
  }
  return dataFormObject;
}
function pullDataToForm(idForm,data){
  if (typeof data === 'object' && data !== null) {
    for (var key in data) {
      if (data.hasOwnProperty(key)) {
        $(`#${idForm} [name="${key}"]`).val(data[key]);
      }
    }
  }
}

function clearForm(idForm,data){
  if (typeof data === 'object' && data !== null) {
    for (var key in data) {
      if (data.hasOwnProperty(key)) {
        data[key]=""
      }
    }
    pullDataToForm(idForm,data)
  }
}


$(document).ready(function() {
  var urlBase = "/bill";
  var objectName = "bill"
  var tableName = "dataTable"

  var tableBillDetail;

  function formatDateTime(inputDate) {
    if (inputDate==null){
      return "null";
    }
    const date = new Date(inputDate);
    const day = ("0" + date.getDate()).slice(-2);
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const year = date.getFullYear();
    const hours = ("0" + date.getHours()).slice(-2);
    const minutes = ("0" + date.getMinutes()).slice(-2);
    const seconds = ("0" + date.getSeconds()).slice(-2);

    return `${hours}:${minutes}:${seconds} ${day}-${month}-${year}`;
  }
  var table = $(`#${tableName}`).DataTable({
    "processing": true,
    "serverSide": true,
    order: [],
    "ajax": {
      "url": urlBase,
      "type": "GET",
      "data": function(d) {
        d.phoneNumber =$("#form-filter input[name='phoneNumber']").val();
        d.startTime =$("#form-filter input[name='startTime']").val()
        d.endTime =$("#form-filter input[name='endTime']").val()
        d.paymentType =$("#form-filter select[name='paymentType']").val()
        d.type = $("#form-filter select[name='type']").val();
      },
      "dataSrc": function(json) {
        return json.data;
      }
    },
    "columns": [
      { "data": "id" },
      { "data": "employeeName" ,"orderable": false,},
      { "data": "customerName","orderable": false, },
      { "data": "paymentType" ,
        "render": function(data, type, row) {
          if (data == -1) {
            return "Chưa chọn";
          } else  if (data == 0) {
            return "Tiền mặt";
          } else if (data == 1) {
            return "Chuyển khoản";
          } else {
            return "";
          }
        }},
      {
        "data": "billCreateDate",
        "render": function(data, type, row) {
         return formatDateTime(data)
        }
      },
      { "data": "shipeFee" },
      { "data": "paymentAmount",
        "render": function(data, type, row) {
          return data.toLocaleString('en-US')
        }
      },
      { "data": "phoneNumber" },
      {
        "data": "type",
        "render": function(data, type, row) {
          if (data == 0) {
            return "Đã hủy";
          } else if (data == 1) {
            return "Chờ xử lí";
          } else if (data == 2) {
            return "Chờ xác thực";
          } else if (data == 3) {
            return "Đã xác thực";
          } else if (data == 4) {
            return "Chờ giao hàng";
          } else if (data == 5) {
            return "Đang giao hàng";
          } else if (data == 6) {
            return "Đã nhận hàng";
          } else if (data == 7) {
            return "Đã hoàn thành";
          } else {
            return "";
          }
        }
      },
      {
        "data": null,
        "orderable": false,
        "render": function(data, type, row) {
          // Nội dung HTML của cột tiếp theo
          return `<button class="btn btn-info btn-circle-sm btn-view-update"><i class="fas fa-info-circle"></i></button>`;
        }
      }
    ],
    "drawCallback": function(settings) {
      $(`#${tableName} tbody tr`).on('click', '.btn-view-update', loadDataModal);
    },
    searchDelay: 1500,
    "paging": true,
    "pageLength": 10,
    "lengthMenu": [10, 25, 50, 100],
  });

  $(`#${tableName}_filter input`).on('keypress', function(event) {
    // Kiểm tra mã phím
    if (event.keyCode === 13) {
      let searchValue = $(this).val();
      table.search(searchValue).draw();
    }
  });


  // Show form update
  $(`#${tableName} tbody`).on('dblclick', 'tr', loadDataModal);
  $(`#btnThanhToanVNPAY`).on('click', '', thanhToanVNPAY);
  $(`#reloadBillDetail`).on('click', '', reloadBillDetail);
  $(`#reloadBill`).on('click', '', function (){
    table.ajax.reload(null,false);
  });

  function thanhToanVNPAY(){
    let billId = $("#form-bill-update input[name=id]").val();
    if (billId){
      $.ajax({
        url: "http://localhost:8080/api/vnpay/"+billId+"?admin=x",
        type: 'GET',
        success: function(response) {
          console.log(response.message);
          window.open(response.message, '_blank');
        },
        error: function(xhr, status, error) {
          alert("Lỗi : "+xhr?.responseText)
        }
      });
    }else {
      alert("Kiểm tra lại hóa đơn !")
    }

  }
  function reloadBillDetail() {

    tableBillDetail.ajax.reload(null,false);
    let billId = $("#form-bill-update input[name=id]").val();
    $.ajax({
      url: urlBase+'/' + billId,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        let data = response;
        pullDataToForm(`form-${objectName}-update`,data)
        $(`#view-update input[name="paymentTime"]`).val(formatDateTime(data.paymentTime))

        $(`#view-update input[name="employee.id"]`).val((data?.employee?.id))
        $(`#view-update input[name="employee.name"]`).val((data?.employee?.name))

        $(`#view-update input[name="customer.id"]`).val((data?.customer?.id))
        $(`#view-update input[name="customer.name"]`).val((data?.customer?.name))

        $(`#view-update input[name="paymentEmployee.id"]`).val((data?.paymentEmployee?.id))
        $(`#view-update input[name="paymentEmployee.name"]`).val((data?.paymentEmployee?.name))

      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu ")
      }
    });

    $.ajax({
      url: "/selloff/calculate-money"+'/' + rowData.id,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        let data = response;
        $(`#view-update input[name="soTienCuaDon"]`).val((data))

      },
      error: function(xhr, status, error) {
        if (xhr.status === 400) {
          // Nếu là lỗi Bad Request, hiển thị thông báo lỗi
          alert("Lỗi: " + xhr.responseText);
        } else {
          // Xử lý các trường hợp lỗi khác nếu cần
          alert("Không thể lấy dữ liệu");
        }
      }
    });

  }


  function loadDataModal() {
    selectRow($(this).closest("tr"))
    tableBillDetail.ajax.reload(null,false);
    let rowData = table.row($(this).closest('tr')).data();
    $.ajax({
      url: urlBase+'/' + rowData.id,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        let data = response;
        $('#view-update').modal('show');
        pullDataToForm(`form-${objectName}-update`,data)
        $(`#view-update input[name="paymentTime"]`).val(formatDateTime(data.paymentTime))

        $(`#view-update input[name="employee.id"]`).val((data?.employee?.id))
        $(`#view-update input[name="employee.name"]`).val((data?.employee?.name))

        $(`#view-update input[name="customer.id"]`).val((data?.customer?.id))
        $(`#view-update input[name="customer.name"]`).val((data?.customer?.name))

        $(`#view-update input[name="paymentEmployee.id"]`).val((data?.paymentEmployee?.id))
        $(`#view-update input[name="paymentEmployee.name"]`).val((data?.paymentEmployee?.name))

      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu ")
      }
    });

    $.ajax({
      url: "/selloff/calculate-money"+'/' + rowData.id,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        let data = response;
        $(`#view-update input[name="soTienCuaDon"]`).val((data))

      },
      error: function(xhr, status, error) {
        if (xhr.status === 400) {
          // Nếu là lỗi Bad Request, hiển thị thông báo lỗi
          alert("Lỗi: " + xhr.responseText);
        } else {
          // Xử lý các trường hợp lỗi khác nếu cần
          alert("Không thể lấy dữ liệu");
        }
      }
    });

  }


  function selectRow(row){
    $('#dataTable tbody tr').removeClass('selected-row');
    row.addClass('selected-row');
  }

  tableBillDetail = $(`#tableBillDetail`).DataTable({
    "processing": true,
    "serverSide": true,
    order: [],
    "ajax": {
      "url": "/bill-detail",
      "type": "GET",
      "data": function(d) {
        var rowData = table.row('.selected-row').data();
        d.billId = rowData?.id
      },
      "dataSrc": function(json) {
        return json.data;
      }
    },
    "columns": [
      {
        "data": "product.id",
      },
      {
        "data": "product.name",
      },
      {
        "data": "productDetail.color",
        "render": function(data, type, row) {
          if (data){
            return  data.name+"("+data.code+")"
          }else {
            return "";
          }
        }
      },
      {
        "data": "productDetail.size",
        "render": function(data, type, row) {
          if (data){
            return  data.size+"("+data.code+")"
          }else {
            return "";
          }
        }
      }, {"data":"quantity",
        "render": function (data, type, row, meta) {
          if (type === 'display') {
            return new Intl.NumberFormat('en-US').format(data);
          }
          return data;
        }
      },
      {"data":"price",
        "render": function (data, type, row, meta) {
          if (type === 'display') {
            return new Intl.NumberFormat('en-US').format(data);
          }
          return data;
        }
      },
      {
        "data": "type",
        "render": function(data, type, row) {
          if (data == 0) {
            return "Không hoạt động";
          } else if (data == 1) {
            return "Hoạt động";
          } else {
            return "";
          }
        }
      }
    ],
    "drawCallback": function(settings) {

    },
    searchDelay: 1500,
    "paging": true,
    "pageLength": 10,
    "lengthMenu": [10, 25, 50, 100],
  });


  // Sự kiện submit form Update
  $(`#form-${objectName}-update`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    formData.delete("employee.id")
    formData.delete("employee.name")
    formData.delete("customer.id")
    formData.delete("customer.name")
    formData.delete("paymentEmployee.id")
    formData.delete("paymentEmployee.name")
    formData.delete("soTienCuaDon")
    formData.delete("phoneNumber")
    formData.delete("paymentTime")

      $.ajax({
        url: urlBase+"/update/"+ formData.get("id"),
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được sửa thành công!');
          table.ajax.reload(null, false);
          clearForm(`form-${objectName}-add`, response)
          $('#view-update').modal('hide');
        },
        error: function (xhr, status, error) {
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectName}-add`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi sửa dữ liệu: ' + xhr.responseText);
            }
          }else {
            alert('Lỗi :' + error);
          }
        }
      });

  });









});

