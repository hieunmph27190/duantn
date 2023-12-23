function converArrayToObject(dataForm){
    let jsonData = {};
    if (Array.isArray(dataForm)){
        dataForm.forEach((value, key) => {
            jsonData[key] = value;
        });
    }
    return jsonData;
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

    var urlBase = "/product";
    var objectName = "product"
    var tableName = "dataTable"

    var urlBaseDetail = "/product-detail";
    var objectNameDetail = "product-detail"
    var tableNameDetail = "tableChiTiet"

    var tableGioHangName = "tableGioHang";
    var tableGioHang;
    var dataGioHang = [];
    var tongSanPham =0;
    var tongTien =0;


    var filterOptions = {}
    var table ;
    var tableChiTiet;


    $("#view-filter #btn-filter").click(function (event) {
        $("#view-filter select[name*='IDs']").each(function() {
            if ($(this).val()!=""){
                filterOptions[$(this).attr("name")]=$(this).val()
            }else{
                filterOptions[$(this).attr("name")]=null
            }
        })
        $("#view-filter input[name*='Price']").each(function() {
            if ($(this).val()!=""){
                filterOptions[$(this).attr("name")]=$(this).val()
            }else{
                filterOptions[$(this).attr("name")]=null
            }
        })
        $("#view-filter select[name*='type']").each(function() {
            if ($(this).val()!=""){
                filterOptions[$(this).attr("name")]=$(this).val()
            }else{
                filterOptions[$(this).attr("name")]=null
            }
        })
        let check = false;
        for (let key in filterOptions) {
            if (filterOptions.hasOwnProperty(key)) {
                if(filterOptions[key]){
                    check=true;
                }
            }
        }
        if (check){
            $('#btn-view-filter').addClass("filter-active")
        }else {
            $('#btn-view-filter').removeClass("filter-active")
        }
        table.ajax.reload(null, false);
        $('#view-filter').modal('hide');
    })

    function fillFilter(filterOptions){
        for (let key in filterOptions) {
            if (filterOptions.hasOwnProperty(key)) {
                let value = filterOptions[key];
                let formElement = $('#view-filter [name="' + key + '"]');
                formElement.val(value);
                formElement.trigger('change');
            }
        }
    }
    $('#btn-view-filter').click(function (){
        fillFilter(filterOptions);
        $('#view-filter').modal('show');
    });
    $('#btn-add-khach-hang').click(function (){
        fillFilter(filterOptions);
        $('#form-customer-add input[name*="phoneNumber"]').val($("#phoneNumber").val())
        $('#view-add-kh').modal('show');
    });

    $("#view-filter #btn-clear").click(function (){
        let filterOptionsClone = {}
        for (let key in filterOptions) {
            if (filterOptions.hasOwnProperty(key)) {
                filterOptionsClone[key]=null;
            }
        }
        fillFilter(filterOptionsClone)
    })

    $("#view-filter #btn-restore").click(function (){
        fillFilter(filterOptions)
    })

    $(`#form-customer-add`).on('submit', function(e) {
        e.preventDefault();
        let formData = new FormData(this);
        if ($(this).valid()) {
            // Gửi AJAX request để cập nhật dữ liệu
            $.ajax({
                url: "/customer/fast",
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (response) {
                    // Xử lý thành công
                    alert('Dữ liệu đã được thêm thành công!');
                    $("#phoneNumber").val(response.phoneNumber);
                    clearForm(`form-customer-add`, response)
                    phoneNumberChange();
                    $('#view-add-kh').modal('hide');
                },
                error: function (xhr, status, error) {
                    if(xhr.status==400){
                        let errorResponse = xhr.responseJSON;
                        if (errorResponse){
                            $(`#form-customer-add`).validate().showErrors(errorResponse);
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


     tableGioHang = $('#tableGioHang').DataTable({
        data: dataGioHang,
        columns: [
            { data: "code" },
            { data: "name" },
            { data: "color" },
            { data: "size" },
            {
                "data":"price",
                "render": function (data, type, row, meta) {
                    if (type === 'display') {
                        return new Intl.NumberFormat('en-US').format(data);
                    }
                    return data;
                }
            },
            {
                data: "quantity" ,
                render: function(data, type, row) {
                    if (type === 'display') {
                        return '<input style="width: 75px;" min="1" step="1" type="number" value="' + data + '">';
                    }
                    return data;
                }
            },
            {
                data: null,
                defaultContent: '<button class="btn btn-danger btn-delete"><i class="fas fa-trash"></i></button>'
            }
        ],
         "drawCallback": function(settings) {
             $(`#tableGioHang tbody tr`).on('click', '.btn-delete',deleteItemGioHang);
         },
         "paging": true,
         "pageLength": 5,
         "lengthMenu": [1,2,3,4,5,8,10, 25, 50, 100]
    });

    // Đặt sự kiện change cho input số lượng
    $('#tableGioHang tbody').on('change', 'input[type="number"]', function() {
        var quantity = $(this).val();
        // Kiểm tra xem giá trị có phải là số nguyên dương không
        if (!Number.isInteger(parseFloat(quantity))) {
            alert('Số lượng sản phẩm phải là một số nguyên.');
            // Đặt lại giá trị về 1 hoặc giá trị mặc định mong muốn
            $(this).val(1);
        } else if (parseInt(quantity) <= 0) {
            alert('Số lượng sản phẩm trong giỏ hàng phải lớn hơn 0.');
            // Đặt lại giá trị về 1 hoặc giá trị mặc định mong muốn
            $(this).val(1);
        }
    });



    // duoi
    $('#tableGioHang').on('change', 'input[type="number"]', function() {
        let newValue = $(this).val();
        tableGioHang.cell($(this).closest('td')).data(newValue)
        tinhtoantong();
    });


    $('.donHang').on('change', 'input[name="thanhToan"]:checked', function() {
        if ($(this).val()==0){
            $("#inputTinhTienThua").removeClass("hidden")
        }else {
            $("#inputTinhTienThua").addClass("hidden")
            $("#tinhTienThua").prop("checked", false)
            $("#view-tinhTienThua").addClass("hidden")
        }
    });


    $('.donHang').on('change', '#tienDua', function() {
        $("#tienThua").text(new Intl.NumberFormat('en-US').format($(this).val()-tongTien));
    });

    $('.donHang').on('change', '#tinhTienThua', function() {
        if ($(this).is(":checked")) {
            $("#view-tinhTienThua").removeClass("hidden")
            $("#tienThua").text(new Intl.NumberFormat('en-US').format($("#tienDua").val()-tongTien));
        } else {
            $("#view-tinhTienThua").addClass("hidden")
        }

    });


    function tinhtoantong() {
        let sanPhamss = tableGioHang.rows().data().toArray();
        let sanPhams = [];
        sanPhamss.forEach((item)=>{
            sanPhams.push({id:item.id,quantity: item.quantity})
        });
        tongSanPham=0;
        sanPhamss.forEach((item) => {
            tongSanPham+=item.quantity*1;
        });
        $.ajax({
            url: "/selloff/calculate-money",
            type: 'POST',
            data: convertToFornData({"sanPhams":sanPhams}),
            contentType: false,
            processData: false,
            success: function (response) {
                tongTien = Number(response);
                console.log(response);
                $("#tongSoLuongSP").text(new Intl.NumberFormat('en-US').format(tongSanPham));
                $("#tongTien").text(new Intl.NumberFormat('en-US').format(tongTien));
                $("#tienThua").text(new Intl.NumberFormat('en-US').format($("#tienDua").val()-tongTien));
            },
            error: function (xhr, status, error) {
                if(xhr.status==400){
                    if(xhr.responseText) {
                        alert('Lỗi khi thêm dữ liệu: ' + xhr.responseText);
                    }else {
                        alert('Lỗi :' + error);
                    }
                }else {
                    alert('Lỗi :' + error);
                }
            }
        });
    }





    function phoneNumberChange() {
        let phoneNumber = $("#phoneNumber").val();
        const phoneNumberPattern = /^(0[1-9][0-9]{8})$/;
        let label = $("#tenKhachHang");
        let inputIDKH = $(".donHang input[name='idKH']");
        if (phoneNumberPattern.test(phoneNumber)){
            $.ajax({
                url: "/customer/phone-number?phoneNumber="+phoneNumber,
                type: 'GET',
                success: function(response) {
                    inputIDKH.val(response.id==undefined?"":response?.id)
                    label.text(response.name==undefined?"NULL":response?.name) ;
                },
                error: function(xhr, status, error) {
                    inputIDKH.val("")
                    label.text("Error");
                }
            });
        }else {
            label.text("Số điện thoại chưa đúng");
            inputIDKH.val("")
        }
        if (phoneNumber==""){
            label.text("NULL");
            inputIDKH.val("")
        }
    }
    $('#phoneNumber').on('change', '',phoneNumberChange);
    function clearAll(){
        tableGioHang.clear().draw();
        $("#phoneNumber").val("");
        phoneNumberChange();
        $("#tienDua").val("0");
        $(".donHang textarea[name='note']").val("");
        tinhtoantong();
    }
    $('#donHangSubmit').on('click', '', function() {
        let data = {};
        let sanPhamss = tableGioHang.rows().data().toArray();
        let sanPhams = [];
        sanPhamss.forEach((item)=>{
            sanPhams.push({id:item.id,quantity: item.quantity})
        });
        data["sanPhams"]=sanPhams;
        if ($(".donHang input[name='idKH']").val()==""||$(".donHang input[name='idKH']").val()){
            data["idKhachHang"]= $(".donHang input[name='idKH']").val();
        }
        data["thanhToan"]= $(".donHang input[name='thanhToan']:checked").val();
        data["trangThaiTT"]=1;
        data["note"]= $(".donHang textarea[name='note']").val();
        if (data.sanPhams.length==0){
            alert("Giỏ hàng trống")
            return;
        }
        if (!confirm("Bạn có chắc chắn muốn tạo đơn không?"+($(".donHang input[name='idKH']").val()==""?"KH :Vãng lai":""))){
            return;
        }

        $.ajax({
            url: "/selloff",
            type: 'POST',
            data: convertToFornData(data),
            contentType: false,
            processData: false,
            success: function (response) {
                clearAll();
                alert("Thành công : "+response);
                if ($(".donHang input[name='thanhToan']:checked").val()==1){
                    thanhToanVNPAY(response);
                }
            },
            error: function (xhr, status, error) {
                if(xhr.status==400){
                    if(xhr.responseText) {
                        alert('Lỗi khi thêm dữ liệu: ' + xhr.responseText);
                    }else {
                        alert('Lỗi :' + error);
                    }
                }else {
                    alert('Lỗi :' + error);
                }
            }
        });
    });

    function thanhToanVNPAY(billId){
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

    table = $(`#${tableName}`).DataTable({
        "processing": true,
        "serverSide": true,
        order: [],
        "ajax": {
            "url": urlBase,
            "type": "GET",
            "data": function(d) {
                for (let key in filterOptions) {
                    if (filterOptions.hasOwnProperty(key)) {
                        if (filterOptions[key] && Array.isArray(filterOptions[key])){
                            d[key] = filterOptions[key];
                        }else {
                            d[key] = filterOptions[key];
                        }
                    }
                }
            },
            "dataSrc": function(json) {
                return json.data;
            }
        },
        "columns": [

            { "data": "code" },
            { "data": "name" },
            {
                "data": "brand.name",
                "render": function(data, type, row) {
                    if (data){
                        return data;
                    }else {
                        return "";
                    }
                }
            },
            {
                "data": "category.name",
                "render": function(data, type, row) {
                    if (data){
                        return data;
                    }else {
                        return "";
                    }
                }
            },
            {
                "data": "sole.name",
                "render": function(data, type, row) {
                    if (data){
                        return data;
                    }else {
                        return "";
                    }
                }
            },
            {
                "data": "imageId",
                "orderable": false,
                "render": function(data, type, row) {
                    if (data){
                        return "<img class='image' src='/product/"+data+"/image' width='60px' height='80px'>"
                    }else {
                        return "<div class='image'>null</div>"
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
                    } else {
                        return "";
                    }
                }
            }


        ],
        "drawCallback": function(settings) {
            $(`#${tableName} tbody tr`).on('click', '.image', showImg);
        },
        searchDelay: 1500,
        "paging": true,
        "pageLength": 100,
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
    $(`#${tableName} tbody`).on('dblclick', 'tr', showDetail);
    $('#btn-view-add').click(function (){
        $('#view-add').modal('show');
    });

    function showDetail(){
        let row = table.row($(this).closest('tr'))
        let rowData = row.data();
        $('#view-detail').modal('show');
        $('#view-detail input[name="id"]').val(rowData.id)
        selectRow(row.nodes().to$())
        tableChiTiet.ajax.reload(null,false);
    }
    function deleteItemGioHang(){
        let row = tableGioHang.row($(this).closest('tr'))
        row.remove().draw(false);
        tinhtoantong();
    }


    function showImg(){
        let row = table.row($(this).closest('tr'))
        let rowData = row.data();
        $('#view-detail-img-update ').modal('show');
        $('#view-detail-img-update #form-product-image-add input[name="id"]').val(rowData.id)
        selectRow(row.nodes().to$())
        tableImg.ajax.reload(null,false);
    }

    function convertToFornData(data) {
        let formData = new FormData();
        data.sanPhams.forEach(function (item, index) {
            for (let key in item) {
                if (item.hasOwnProperty(key)) {
                    let fieldName = 'sanPhams[' + index + '].' + key;
                    formData.append(fieldName, item[key]);
                }
            }
        });
        for (let key in data) {
            if (data.hasOwnProperty(key) && key !== 'sanPhams') {
                formData.append(key, data[key]);
            }
        }
        return formData;
    }



    tableChiTiet = $(`#tableChiTiet`).DataTable({
        "processing": true,
        "serverSide": true,
        order: [],
        "ajax": {
            "url": urlBaseDetail,
            "type": "GET",
            "data": function(d) {
                var rowData = table.row('.selected-row').data();
                d.idProduct = rowData?.id
            },
            "dataSrc": function(json) {
                return json.data;
            }
        },
        "columns": [
            { "data": "id" },
            {
                "data": "color",
                "render": function(data, type, row) {
                    if (data){
                        return data.name+"("+data.code+")";
                    }else {
                        return "";
                    }
                }
            },
            {
                "data": "size",
                "render": function(data, type, row) {
                    if (data){
                        return  data.size+"("+data.code+")"
                    }else {
                        return "";
                    }
                }
            }, {"data":"amount",
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
            },
            {
                "data": null,
                "orderable": false,
                "render": function(data, type, row) {
                    // Nội dung HTML của cột tiếp theo
                    return `<button class="btn btn-info btn-circle-sm btn-add-gio-hang">+</button>`;
                }
            }
        ],
        "drawCallback": function(settings) {
            $(`#tableChiTiet tbody tr`).on('click', '.btn-add-gio-hang',addGioHang);
        },
        searchDelay: 1500,
        "paging": true,
        "pageLength": 10,
        "lengthMenu": [10, 25, 50, 100],
    });

    $(`#${tableNameDetail}_filter input`).on('keypress', function(event) {
        // Kiểm tra mã phím
        if (event.keyCode === 13) {
            let searchValue = $(this).val();
            tableChiTiet.search(searchValue).draw();
        }
    });




    function addGioHang() {
        let rowDataDetail = tableChiTiet.row($(this).closest('tr')).data();
        var rowDataTable = table.row('.selected-row').data();
        let checkTonTai = false;
        tableGioHang.rows().data().toArray().forEach((item) => {
            if (rowDataDetail.id == item.id){
                checkTonTai =true;
            }
        });
        if (checkTonTai){
            alert("Đã có trong giỏ hàng");
            return;
        }

        let newRowData = {
            "id":rowDataDetail.id,
            "code": rowDataTable.code,
            "name": rowDataTable.name,
            "color": rowDataDetail.color.name+"("+rowDataDetail.color.code+")",
            "size": rowDataDetail.size.size+"("+rowDataDetail.size.code+")",
            "price": rowDataDetail.price,
            "quantity": 1
        };
        tableGioHang.row.add(newRowData).draw(false);
        tinhtoantong();
        $('#view-detail').modal('hide');
    }









    // chọn row
    function selectRow(row){
        $('#dataTable tbody tr').removeClass('selected-row');
        row.addClass('selected-row');
    }

    $('#dataTable tbody').on('click', 'tr', function() {
        selectRow($(this))
        tableChiTiet.ajax.reload(null,false);
    });

    var selectedFilesUpdate = [];
    var inputFileUpdate = $('#fileInputUpdate').on('change', function() {
        selectedFilesUpdate = loadfile(this,inputFileUpdate,selectedFilesUpdate,"imagePreviewUpdate")
    });



    var selectedFiles = [];
    var inputFile = $('#fileInput').on('change', function() {
        selectedFiles = loadfile(this,inputFile,selectedFiles,"imagePreview")
    });


    function loadfile(thiss,inputFilex,selectedFilesx,imagePreviewID){
        let fileList = thiss.files;
        let imagePreview = $(`#${imagePreviewID}`);
        for (var i = 0; i < fileList.length; i++) {
            let file = fileList[i];
            selectedFilesx.push(file);
        }
        updateInputFileValue(inputFilex,selectedFilesx)
        imagePreview.empty();
        // Duyệt qua danh sách các tệp đã chọn
        for (let i = 0; i < selectedFilesx.length; i++) {
            let file = selectedFilesx[i];
            let reader = new FileReader();

            // Đọc và hiển thị ảnh lên giao diện
            reader.onload = function(event) {
                let image = $('<img>').attr('src', event.target.result);
                image.attr("width","150px");
                image.attr("height","200px");
                var deleteButton = $('<button>').text('x').addClass('delete-button');
                var imageContainer = $('<div>').addClass('image-container p-3 border-dark ').append(image, deleteButton);
                imagePreview.append(imageContainer);
                // Thêm sự kiện click cho nút xóa
                deleteButton.on('click', function() {
                    $(this).parent().remove();
                    selectedFiles.splice(selectedFilesx.indexOf(file), 1);
                    updateInputFileValue(inputFile, selectedFiles);
                });
            };

            reader.readAsDataURL(file);
        }
        return selectedFilesx;
    }
    function clearSelectedFiles(inputFilex, selectedFilesx,imagePreviewID) {
        // Xóa các tệp đã chọn
        selectedFilesx = [];

        // Cập nhật lại giá trị của input file để xóa dữ liệu
        updateInputFileValue(inputFilex,selectedFilesx);

        // Xóa hết các ảnh hiện có trong imagePreview
        let imagePreview = $(`#${imagePreviewID}`);
        imagePreview.empty();
        return selectedFilesx;
    }

    // Hàm cập nhật giá trị của input file với mảng selectedFiles
    function updateInputFileValue(inputFilex,selectedFilesx) {

        let fileList = new DataTransfer();
        // Thêm các tệp vào fileList
        for (let i = 0; i < selectedFilesx.length; i++) {
            fileList.items.add(selectedFilesx[i]);
        }
        if (inputFilex[0].files) {
            inputFilex[0].files = fileList.files; // Gán lại các tệp từ newFileList vào input file
        }
    }




    $(`.modal-body select[name*=".id"]`).each((index,element)=>{
        $(element).select2({
            maximumSelectionLength: 1,
            ajax: {
                url: '/'+element.name.replace(".id",""),
                type: 'GET',
                dataType: 'json',
                delay: 250,
                data: function(params) {
                    return {
                        "search[value]": params.term
                    };
                },
                processResults: function(data) {

                    return {
                        results: data.data.map(item => {
                            return {
                                id: item.id,
                                text: (item.name||item.size)+"("+item.code+")"
                            };
                        })
                    };
                },
                cache: true
            }
        });
    })

    $(`#filter select[name*="IDs"]`).each((index,element)=>{
        $(element).select2({
            ajax: {
                url: '/'+element.name.replace("IDs",""),
                type: 'GET',
                dataType: 'json',
                delay: 250,
                data: function(params) {
                    return {
                        "search[value]": params.term
                    };
                },
                processResults: function(data) {

                    return {
                        results: data.data.map(item => {
                            return {
                                id: item.id,
                                text: (item.name||item.size)+"("+item.code+")"
                            };
                        })
                    };
                },
                cache: true
            }
        });
    })



    var configValidateDetail = {
        rules: {
            "product.id": {
                required: true,
            },
            amount: {
                required: true
            },
            price: {
                required: true
            },
            "color.id": {
                required: true
            },
            "size.id": {
                required: true
            },
            type: {
                required: true
            }
        },
        messages: {
            "product.id": {
                required: "Vui lòng nhập trường này",
            },
            amount: {
                required: "Vui lòng nhập trường này"
            },
            price: {
                required: "Vui lòng nhập trường này"
            },
            "color.id": {
                required: "Vui lòng nhập trường này"
            },
            "size.id": {
                required: "Vui lòng nhập trường này"
            },
            type: {
                required: "Vui lòng chọn trường này"
            }
        }
    }


// Validate form add
    $(`#form-${objectNameDetail}-add`).validate(configValidateDetail);
// Validate form update
    $(`#form-${objectNameDetail}-update`).validate({
        rules:{...configValidateDetail.rules,id:{required:true}},
        messages:{...configValidateDetail.messages,id:{required:"Vui lòng nhập trường này"}}
    });



    var tableImg = $(`#tableImg`).DataTable({
        searching: false,
        "processing": true,
        "serverSide": true,
        "ajax": {
            "url": "/image",
            "type": "GET",
            "data": function(d) {
                d.idProduct =table.row('.selected-row').data()?.id
            },
            "dataSrc": function(json) {
                return json.data;
            }
        },
        "columns": [
            {data: "id"},
            {
                "data": "id",
                "orderable": false,
                "render": function(data, type, row) {
                    if (data){
                        return "<img src='/product/"+data+"/image' width='120px' height='160px'>"
                    }else {
                        return "null"
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
                    }else if (data == 2) {
                        return "Ảnh chính";
                    } else {
                        return "";
                    }
                }
            }
        ],

        searchDelay: 1500,
        "paging": true,
        "pageLength": 10,
        "lengthMenu": [10, 25, 50, 100],
    });


});

