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
  var urlBase = "/sole";
  var objectName = "sole"
  var tableName = "dataTable"
  var table = $(`#${tableName}`).DataTable({
    order: [],
    "processing": true,
    "serverSide": true,
    "ajax": {
      "url": urlBase,
      "type": "GET",
      "data": function(d) {
        d.callAll = true
      },
      "dataSrc": function(json) {
        return json.data;
      }
    },
    "columns": [
      { "data": "id" },
      { "data": "code" },
      { "data": "name" },
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
      },
      {
        "data": null,
        "orderable": false,
        "render": function(data, type, row) {
          // Nội dung HTML của cột tiếp theo
          return `<button class="btn btn-info btn-circle-sm btn-view-update"><i class="fas fa-info-circle"></i></button>
                    <button class="btn btn-info btn btn-danger btn-circle-sm btn-delete"><i class="fas fa-trash"></i></button>`;
        }
      }
    ],
    "drawCallback": function(settings) {
      $(`#${tableName} tbody tr`).on('click', '.btn-view-update', loadDataModal);
      $(`#${tableName} tbody tr`).on('click', '.btn-delete',deleteDL);
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
  $('#btn-view-add').click(function (){
    $('#view-add').modal('show');
  });

  function loadDataModal() {
    let rowData = table.row($(this).closest('tr')).data();
    $.ajax({
      url: urlBase+'/' + rowData.id,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        let data = response;
        $('#view-update').modal('show');
        pullDataToForm(`form-${objectName}-update`,data)
      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu")
      }
    });

  }


  // Sự kiện submit form Add
  $(`#form-${objectName}-add`).on('submit', function(e) {
    e.preventDefault();
    if ($(this).valid()) {
      // Gửi AJAX request để cập nhật dữ liệu
      $.ajax({
        url: urlBase,
        type: 'POST',
        data: JSON.stringify(converArrayToObject($(`#form-${objectName}-add`).serializeArray())),
        contentType: 'application/json',
        processData: true,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được thêm thành công!');
          table.ajax.reload(null, false);
          clearForm(`form-${objectName}-add`, response)
          $('#view-add').modal('hide');
        },
        error: function (xhr, status, error) {
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectName}-add`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi thêm dữ liệu: ' + xhr.responseText);
            }
          }else {
            alert('Lỗi :' + error);
          }
        }
      });
    }
  });




  // Sự kiện submit form Update
  $(`#form-${objectName}-update`).on('submit', function(e) {
    e.preventDefault();
    if ($(this).valid()) {
      // Lấy giá trị từ các trường input
      let dataForm = $(`#form-${objectName}-update`).serializeArray();
      let data = converArrayToObject(dataForm)
      // Gửi AJAX request để cập nhật dữ liệu
      $.ajax({
        url: urlBase+'/' + data.id,
        type: 'PUT',
        data: JSON.stringify(data),
        contentType: 'application/json',
        processData: true,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được cập nhật thành công!');
          table.order([])
          table.ajax.reload(null, false);
          $('#view-update').modal('hide');
        },
        error: function (xhr, status, error) {
          // Xử lý lỗi nếu cần thiết
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-${objectName}-update`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi cập nhật dữ liệu: ' + xhr.responseText);
            }
            // Hiển thị thông báo lỗi tương ứng với từng trường
          }else {
            alert('Lỗi :' + error);
          }

        }
      });
    }
  });

  //Sự kiện xóa
  function deleteDL() {
    let rowData = table.row($(this).closest('tr')).data();
    if (confirm("Xác nhận xóa")){
      $.ajax({
        url: urlBase+'/' + rowData.id,
        type: 'DELETE',
        success: function(response) {
          table.ajax.reload(null, false);
          alert("Xóa thành công")
        },
        error: function(xhr, status, error) {
          alert('Lỗi :' + xhr.responseText);
        }
      });
    }
  }


  var configValidate = {
    rules: {
      code: {
        required: true,
        minlength: 3,
        noLeadingWhitespace: true, // Thêm quy tắc kiểm tra tùy chỉnh
        noTrailingWhitespace: true // Sử dụng quy tắc mới
      },
      name: {
        required: true,
        minlength: 3,
        noLeadingWhitespace: true, // Thêm quy tắc kiểm tra tùy chỉnh
        noTrailingWhitespace: true // Sử dụng quy tắc mới
      },
      type: {
        required: true
      }
    },
    messages: {
      code: {
        required: "Vui lòng nhập trường này",
        minlength: "Trường này phải có ít nhất 3 ký tự",
        noLeadingWhitespace: "Mã không được chứa khoảng trắng ở đầu dòng",
        noTrailingWhitespace: "Mã không được chứa khoảng trắng ở cuối dòng"

      },
      name: {
        required: "Vui lòng nhập trường này",
        minlength: "Trường này phải có ít nhất 3 ký tự",
        noLeadingWhitespace: "Tên không được chứa khoảng trắng ở đầu dòng",
        noTrailingWhitespace: "name không được chứa khoảng trắng ở cuối dòng"
      },
      type: {
        required: "Vui lòng chọn trường này"
      }
    }
  };

  // Thêm quy tắc kiểm tra tùy chỉnh
  $.validator.addMethod("noWhitespace", function(value) {
    return /^\S+$/.test(value);
  }, "Không được chứa khoảng trắng");

// Thêm quy tắc kiểm tra tùy chỉnh cho việc kiểm tra khoảng trắng ở đầu dòng
  $.validator.addMethod("noLeadingWhitespace", function(value) {
    return /^[^\s]+/.test(value);
  }, "Không được chứa khoảng trắng ở đầu dòng");

// Thêm quy tắc kiểm tra tùy chỉnh cho việc kiểm tra khoảng trắng ở cuối dòng
  $.validator.addMethod("noTrailingWhitespace", function(value) {
    // Kiểm tra xem giá trị sau khi trim có chứa khoảng trắng ở cuối dòng không
    return value === value.replace(/\s+$/, '');
  }, "Không được chứa khoảng trắng ở cuối dòng");




// Validate form add
  $(`#form-${objectName}-add`).validate(configValidate);
// Validate form update
  $(`#form-${objectName}-update`).validate({
    rules:{...configValidate.rules,id:{required:true}},
    messages:{...configValidate.messages,id:{required:"Vui lòng nhập trường này"}}
  });

});

