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
  // Sự kiện submit form Add
  $(`#form-login`).on('submit', function(e) {
    e.preventDefault();
    if ($(this).valid()) {
      // Gửi AJAX request để cập nhật dữ liệu
      $.ajax({
        url: "/api/auth/signin",
        type: 'POST',
        data: JSON.stringify(converArrayToObject($(`#form-login`).serializeArray())),
        contentType: 'application/json',
        processData: true,
        success: function (response) {
          if (response.roles.includes("ROLE_USER")) {
            $.ajax({
              url: "/api/auth/signout",
              type: 'POST',
              data: {},
              contentType: 'application/json',
              processData: true,
              success: function (response) {

              },
              error: function (xhr, status, error) {

              }
            });
            alert("Tài khoản không có quyên truy cập !")
          } else {
            window.location.href = "/selloff/view"
          }
        },
        error: function (xhr, status, error) {
          if(xhr.status==401){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              alert('Thông tin không chính xác : ' + errorResponse.message);
            }else {
              alert('Lỗi login ' + xhr.responseText);
            }
          }else {
            alert('Lỗi :' + xhr.responseText);
          }
        }
      });
    }
  });


  var configValidate = {
    rules: {
      email: {
        required: true,
        minlength: 3
      },
      password: {
        required: true
      }
    },
    messages: {
      email: {
        required: "Vui lòng nhập trường này",
        minlength: "Trường này phải có ít nhất 3 ký tự"
      },
      password: {
        required: "Vui lòng nhập trường này"
      }
    }
  }

  $('#form-login input[name="email"]').change(function (){
    if (!$(this).val().includes("@")){
      $(this).val($(this).val()+"@gmail.com")
    }
  });
  $(`#form-login`).validate(configValidate);
});

