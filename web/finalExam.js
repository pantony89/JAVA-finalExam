/* 
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 * Updated 2015 Mark Russell <mark.russell@lambtoncollege.ca>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*=================================================================
 * Warning. This file is fine. You should not need to change this *
 * file at all to complete this project. If you think you do, ask *
 * the instructor about what you're planning to do.               *
 =================================================================*/

var getProducts = function () {
    $.getJSON('./rs/products', function (data) {
        var result = '<tbody>';
        result += "<tr><th>Product ID</th><th>Name</th><th>Vendor ID</th></tr>";
        for (var i = 0; i < data.length; i++)
            result += '<tr><td>' + data[i].productId + '</td><td>' + data[i].name + '</td><td>' + data[i].vendorId + '</td></tr>';
        result += '</tbody>';
        $('#products').html(result);
    });
};

var getVendors = function () {
    $.getJSON('./rs/vendors', function (data) {
        var result = '<tbody>';
        result += "<tr><th>Vendor ID</th><th>Name</th><th>Contact Name</th><th>Phone Number</th></tr>";
        for (var i = 0; i < data.length; i++)
            result += '<tr><td>' + data[i].vendorId + '</td><td>' +
                    data[i].name + '</td><td>' + data[i].contactName +
                    '</td><td>' + data[i].phoneNumber + '</td></tr>';
        result += '</tbody>';
        $('#vendors').html(result);
    });
};

$(document).ready(function () {
    getProducts();
    getVendors();

    $('#findProductById').click(function () {
        $.ajax({
            url: './rs/products/' + $('#productId').val(),
            method: 'get',
            dataType: 'json',
            success: function (data) {
                $('#productName').val(data.name);
                $('#productVendor').val(data.vendorId);
            }
        });
    });

    $('#deleteProductById').click(function () {
        $.ajax({
            url: './rs/products/' + $('#productId').val(),
            method: 'delete',
            complete: getProducts
        });
    });

    $('#addProduct').click(function () {
        $.ajax({
            url: './rs/products',
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify({
                productId: parseInt($('#productId').val()),
                name: $('#productName').val(),
                vendorId: parseInt($('#productVendor').val())
            }),
            complete: getProducts
        });
    });

    $('#setProduct').click(function () {
        $.ajax({
            url: './rs/products/' + $('#productId').val(),
            method: 'put',
            contentType: 'application/json',
            data: JSON.stringify({
                productId: parseInt($('#productId').val()),
                name: $('#productName').val(),
                vendorId: parseInt($('#productVendor').val())
            }),
            complete: getProducts
        });
    });

    $('#findVendorById').click(function () {
        $.ajax({
            url: './rs/vendors/' + $('#vendorId').val(),
            method: 'get',
            dataType: 'json',
            success: function (data) {
                $('#vendorName').val(data.name);
                $('#vendorContact').val(data.contactName);
                $('#vendorPhone').val(data.phoneNumber);
            }
        });
    });

    $('#deleteVendorById').click(function () {
        $.ajax({
            url: './rs/vendors/' + $('#vendorId').val(),
            method: 'delete',
            complete: getVendors
        });
    });

    $('#addVendor').click(function () {
        $.ajax({
            url: './rs/vendors',
            method: 'post',
            contentType: 'application/json',
            data: JSON.stringify({
                vendorId: parseInt($('#vendorId').val()),
                name: $('#vendorName').val(),
                contactName: $('#vendorContact').val(),
                phoneNumber: $('#vendorPhone').val()
            }),
            complete: getVendors
        });
    });

    $('#setVendor').click(function () {
        $.ajax({
            url: './rs/vendors/' + $('#vendorId').val(),
            method: 'put',
            contentType: 'application/json',
            data: JSON.stringify({
                vendorId: parseInt($('#vendorId').val()),
                name: $('#vendorName').val(),
                contactName: $('#vendorContact').val(),
                phoneNumber: $('#vendorPhone').val()
            }),
            complete: getVendors
        });
    });
});