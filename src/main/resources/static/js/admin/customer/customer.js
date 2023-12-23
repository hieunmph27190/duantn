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
  var urlBase = "/customer";
  var objectName = "customer"
  var tableName = "dataTable"
  function formatDate(inputDate) {
    if (inputDate==null){
      return "null";
    }
    const date = new Date(inputDate);
    const day = ("0" + date.getDate()).slice(-2);
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const year = date.getFullYear();
    return `${year}-${month}-${day}`;
  }
  var table = $(`#${tableName}`).DataTable({
    "processing": true,
    "serverSide": true,
    order: [],
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
      { "data": "name" },
      {
        "data": "gender",
        "render": function(data, type, row) {
          if (data==null){
            return  "null";
          }else {
            if (data) {
              return "Nam";
            } else {
              return "Nữ"
            }
          }
        }
      },
      {
        "data": "dateOfBirth",
        "render": function(data, type, row) {
         return formatDate(data)
        }
      },
      {
        "data": "address",
        "render": function(data, type, row) {
              return data + " (d):"+ row.district+" (city):"+row.city;
        }
      },
      { "data": "email" },
      { "data": "phoneNumber" },
      {
        "data": "image",
        "orderable": false,
        "render": function(data, type, row) {
          if (data){
            let randomParam = new Date().getTime();
            return "<img src='"+urlBase+"/"+row.id+"/image?"+randomParam+"=x' width='60px' height='80px' alt='null'>"
          }else {
            return "null";
          }
        }
      },
      {
        "data": "type",
        "render": function(data, type, row) {
          if (data == 0) {
            return "Không hoạt động";
          } else if (data == 1) {
            return "Hoạt động";
          } else if (data == 2) {
            return "Chưa đăng kí";
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
  $(`#resetPass`).on('click', '', resetPass);
  $('#btn-view-add').click(function (){
    $('#view-add').modal('show');
  });

  function resetPass(){
    let formData = new FormData();
    let id = $("#form-customer-update input[name='id']").val();
    let newPassword = prompt("Vui lòng nhập mật khẩu mới:");
    if (newPassword!=null&&newPassword!="") {
      formData.set("id",id);
      formData.set("newPassword",newPassword);
      $.ajax({
        url: urlBase+"/change-password",
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Thay đổi mật khẩu thành công!');
        },
        error: function (xhr, status, error) {
          if(xhr.status==400){

              alert('Lỗi khi sửa dữ liệu: ' + xhr.responseText);

          }else {
            alert('Lỗi :' + error);
          }
        }
      });
    } else if (newPassword =='') {
      alert("Mật khẩu không được để trống");
    }
  }

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
        $(`#view-update input[name="dateOfBirth"]`).val(formatDate(data.dateOfBirth))
        if (data.gender != null){
          $(`#view-update select[name="gender"]`).val(data.gender.toString());
        }


        var inputFileUpdate = $('#fileInputUpdate')
        let fileList = new DataTransfer();
        if (inputFileUpdate[0].files) {
          inputFileUpdate[0].files = fileList.files;
        }


        $("#imagePreviewUpdate").empty()
        if (data.image){
          let randomParam = new Date().getTime();
          let image = $('<img>').attr('src', urlBase+"/"+data.id+"/image?"+randomParam+"=x");
          image.attr("width","165px");
          image.attr("height","220px");
          var imageContainer = $('<div>').addClass('d-flex justify-content-center align-content-center').append(image);
          $("#imagePreviewUpdate").append(imageContainer);
        }

      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu")
      }
    });

  }


  // Sự kiện submit form Add
  $(`#form-${objectName}-add`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    if (formData.getAll('imgs')[0] && !formData.getAll('imgs')[0].name) {
      formData.set('imgs', null);
    }
    if ($(this).valid()) {
      // Gửi AJAX request để cập nhật dữ liệu
      $.ajax({
        url: urlBase,
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được thêm thành công!');
          table.order([])
          table.ajax.reload(null, false);
          clearForm(`form-${objectName}-add`, response)
          let inputFile = $('#fileInput')
          let fileList = new DataTransfer();
          if (inputFile[0].files) {
            inputFile[0].files = fileList.files;
          }
          $("#imagePreview").empty()
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
    let formData = new FormData(this);
    if (formData.getAll('imgs')[0] && !formData.getAll('imgs')[0].name) {
      formData.set('imgs', null);
    }
    if ($(this).valid()) {
      // Gửi AJAX request để cập nhật dữ liệu
      $.ajax({
        url: urlBase+"/"+ formData.get("id"),
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
          }else if(xhr.status==403){
              alert("Không có quyền");
          } else {
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
         if(xhr.status==403){
            alert("Không có quyền");
          }else {
           alert('Lỗi :' + xhr.responseText);
         }

        }
      });
    }
  }


  var configValidate = {
    rules: {
      name: {
        required: true,
        minlength: 4,
        noLeadingWhitespace: true // Thêm quy tắc kiểm tra tùy chỉnh
      },
      gender: {
        required: true
      },
      userName: {
        required: true
      },
      password: {
        required: true,
        minlength: 4,
        noLeadingWhitespace: true // Thêm quy tắc kiểm tra tùy chỉnh
      },
      dateOfBirth: {
        required: true
      },
      phoneNumber: {
        required: true,
        pattern: /^(0|\+\d{1,3})\d{9}$/
      },
      email: {
        required: true
      },
      type: {
        required: true
      }
    },
    messages: {
      name: {
        required: "Vui lòng nhập trường này",
        minlength: "Trường này phải có ít nhất 4 ký tự",
        noLeadingWhitespace: "Tên không được chứa khoảng trắng ở đầu dòng"
      },
      gender: {
        required: "Vui lòng chọn trường này"
      },
      dateOfBirth: {
        required: "Vui lòng chọn trường này"
      },
      phoneNumber: {
        required: "Vui lòng chọn trường này",
        pattern:"Nhập đúng số điện thoại của bạn"
      },
      email: {
        required: "Vui lòng chọn trường này"
      },
      type: {
        required: "Vui lòng chọn trường này"
      }
    }
  }

// Thêm quy tắc kiểm tra tùy chỉnh cho việc kiểm tra khoảng trắng ở đầu dòng
  $.validator.addMethod("noLeadingWhitespace", function(value) {
    return /^[^\s]+/.test(value);
  }, "Không được chứa khoảng trắng ở đầu dòng");


// Validate form add
  $(`#form-${objectName}-add`).validate(configValidate);
// Validate form update
  $(`#form-${objectName}-update`).validate({
    rules:{...configValidate.rules,id:{required:true}},
    messages:{...configValidate.messages,id:{required:"Vui lòng nhập trường này"}}
  });


  var inputFile = $('#fileInput').on('change', function() {
     show(this,inputFile,"imagePreview")
  });
  var inputFileUpdate = $('#fileInputUpdate').on('change', function() {
    show(this,inputFileUpdate,"imagePreviewUpdate")
  });

  function show(thiss,inputFilex,imagePreviewID){
    let fileList = thiss.files;
    let imagePreview = $(`#${imagePreviewID}`);
    imagePreview.empty();
    // Duyệt qua danh sách các tệp đã chọn
    for (let i = 0; i < fileList.length; i++) {
      let file = fileList[i];
      let reader = new FileReader();

      // Đọc và hiển thị ảnh lên giao diện
      reader.onload = function(event) {
        let image = $('<img>').attr('src', event.target.result);
        image.attr("width","165px");
        image.attr("height","220px");
        var imageContainer = $('<div>').addClass('d-flex justify-content-center align-content-center').append(image);
        imagePreview.append(imageContainer);


      };

      reader.readAsDataURL(file);
    }
  }



});

