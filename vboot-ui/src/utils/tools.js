import { get } from './request'
// import { SSL_OP_ALLOW_UNSAFE_LEGACY_RENEGOTIATION } from 'constants';

//数组删除自定元素
export const arrayRemoveByValue = (arr, val) => {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val) {
            arr.splice(i, 1);
            break;
        }
    }
}
//数组批量删除指定元素
export const batchArrayRemoveByArray = (arr, reArr) => {
    reArr.forEach(element => {
        arrayRemoveByValue(arr, element);
    });
}


//获取想要的字典数据
export const getDictInfoById = (id) => {
    return get(`/system/dict/dict/${id}`)
}

//比较两个对象是否完全相等
export const isObjectValueEqual = (a, b) => {

    var aProps = Object.getOwnPropertyNames(a);
    var bProps = Object.getOwnPropertyNames(b);
    //移除数组中的"__ob__"元素
    batchArrayRemoveByArray(aProps, ["avatar", "mail", "mobilephone", "__ob__"]);
    batchArrayRemoveByArray(bProps, ["avatar", "mail", "mobilephone", "__ob__"]);
    if (aProps.length != bProps.length) {
        return false;
    }
    for (var i = 0; i < aProps.length; i++) {
        var propName = aProps[i];
        if (a[propName] !== b[propName]) {
            return false;
        }
    }

    return true;
}

/**
 * 时间戳转具体时间
 */
export const timestampToTime = (timestamp) => {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate()<10 ? ('0'+date.getDate()):date.getDate()) + ' ';
    var h = (date.getHours()<10? ('0'+date.getHours()):date.getHours()) + ':';
    var m = (date.getMinutes()<10?('0'+date.getMinutes()):date.getMinutes())+ ':';
    var s = date.getSeconds()<10?('0'+date.getSeconds()):date.getSeconds();
    return Y+M+D+h+m+s;
}
