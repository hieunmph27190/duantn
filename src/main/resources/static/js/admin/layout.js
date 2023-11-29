function converArrayToObject(dataForm){
  if (Array.isArray(dataForm))
  var dataFormProfileObject = {}
  for (const data of dataForm) {
    dataFormProfileObject[data.name]=data.value
  }
  return dataFormProfileObject;
}
function pullDataToForm(idForm,data){
  if (typeof data === 'object' && data !== null) {
    for (let key in data) {
      if (data.hasOwnProperty(key)) {
        $(`#${idForm} [name="${key}"]`).val(data[key]);
      }
    }
  }
}

function clearForm(idForm,data){
  if (typeof data === 'object' && data !== null) {
    for (let key in data) {
      if (data.hasOwnProperty(key)) {
        data[key]=""
      }
    }
    pullDataToForm(idForm,data)
  }
}
function formatDate(inputDate) {
  const date = new Date(inputDate);
  const day = ("0" + date.getDate()).slice(-2);
  const month = ("0" + (date.getMonth() + 1)).slice(-2);
  const year = date.getFullYear();
  return `${year}-${month}-${day}`;
}


$(document).ready(function() {

  $(`#profile`).on('click', '', loadDataModal);

  function loadDataModal() {
    $.ajax({
      url: '/profile' ,
      type: 'GET',
      success: function(response) {
        // Lấy dữ liệu từ response và hiển thị trên modal
        let data = response;
        $('#view-profile-update').modal('show');
        pullDataToForm(`form-profile-update`,data)
        $(`#view-profile-update input[name="dateOfBirth"]`).val(formatDate(data.dateOfBirth))
        if (data.gender != null){
          $(`#view-profile-update select[name="gender"]`).val(data.gender.toString());
        }


        let inputFileProfileUpdate = $('#fileInputProfileUpdate')
        let fileListProfile = new DataTransfer();
        if (inputFileProfileUpdate[0].files) {
          inputFileProfileUpdate[0].files = fileListProfile.files;
        }


        $("#imageProfilePreviewUpdate").empty()
        if (data.image){
          let randomParam = new Date().getTime();
          let image = $('<img>').attr('src', "/employee/"+data.id+"/image?"+randomParam+"=x");
          image.attr("width","165px");
          image.attr("height","220px");
          let imageContainer = $('<div>').addClass('d-flex justify-content-center align-content-center').append(image);
          $("#imageProfilePreviewUpdate").append(imageContainer);
        }

      },
      error: function(xhr, status, error) {
        alert("Không thể lấy dữ liệu")
      }
    });

  }


  var configValidateProfile = {
    rules: {
      name: {
        required: true
      },
      gender: {
        required: true
      },
      userName: {
        required: true
      },
      password: {
        required: true
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
      address: {
        required: true
      },
      type: {
        required: true
      }
    },
    messages: {
      name: {
        required: "Vui lòng nhập trường này"
      },
      gender: {
        required: "Vui lòng chọn trường này"
      },
      userName: {
        required: "Vui lòng chọn trường này"
      },
      password: {
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
      address: {
        required: "Vui lòng chọn trường này"
      },
      type: {
        required: "Vui lòng chọn trường này"
      }
    }
  }



  $(`#form-profile-update`).validate({
    rules:{...configValidateProfile.rules,id:{required:true}},
    messages:{...configValidateProfile.messages,id:{required:"Vui lòng nhập trường này"}}
  });


  // Sự kiện submit form Update
  $(`#form-profile-update`).on('submit', function(e) {
    e.preventDefault();
    let formData = new FormData(this);
    if (formData.getAll('imagesProfile')[0] && !formData.getAll('imagesProfile')[0].name) {
      formData.set('imagesProfile', null);
    }
    if ($(this).valid()) {
      // Gửi AJAX request để cập nhật dữ liệu
      $.ajax({
        url: "/profile/"+ formData.get("id"),
        type: 'PUT',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
          // Xử lý thành công
          alert('Dữ liệu đã được sửa thành công!');
          $('#view-profile-update').modal('hide');
        },
        error: function (xhr, status, error) {
          if(xhr.status==400){
            let errorResponse = xhr.responseJSON;
            if (errorResponse){
              $(`#form-profile-update`).validate().showErrors(errorResponse);
            }else {
              alert('Lỗi khi sửa dữ liệu: ' + xhr.responseText);
            }
          }else {
            alert('Lỗi :' + error);
          }
        }
      });
    }
  });





  var fileInputProfileUpdate = $('#fileInputProfileUpdate').on('change', function() {
    show(this,fileInputProfileUpdate,"imageProfilePreviewUpdate")
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

