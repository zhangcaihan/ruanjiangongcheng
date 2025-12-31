import CryptoJS from 'crypto-js'

/**
 * SHA256加密函数
 * @param {string} str - 需要加密的字符串
 * @returns {string} 加密后的字符串
 */
export const sha256 = (str) => {
    return CryptoJS.SHA256(str).toString(CryptoJS.enc.Hex)
}

/**
 * 密码加密函数 - 添加前缀以匹配后端加密方式
 * @param {string} password - 原始密码
 * @returns {string} 加密后的密码
 */
export const encryptPassword = (password) => {
    // 这里可以根据后端要求添加特定前缀
    return sha256(password)
}