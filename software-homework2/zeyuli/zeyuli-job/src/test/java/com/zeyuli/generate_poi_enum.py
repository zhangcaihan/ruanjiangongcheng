import pandas as pd
import os

# 读取Excel文件
excel_path = r'd:\fzu\Software\homework2\zeyuli\docs\高德API\POI类型.xlsx'
df = pd.read_excel(excel_path, sheet_name='附录E CMS_POI分类与编码（中英文）')

# 生成Java枚举类内容
enum_content = '''package com.zeyuli.enm;

/**
 * POI类型枚举类
 * 基于高德API POI类型生成
 */
public enum POIEnum {
'''

# 遍历数据生成枚举值
for index, row in df.iterrows():
    # 获取类型代码
    type_code = str(row['NEW_TYPE']).strip() if not pd.isna(row['NEW_TYPE']) else ""
    
    # 获取类型名称，优先使用小类，其次中类，最后大类
    if not pd.isna(row['小类']):
        type_name = str(row['小类']).strip()
    elif not pd.isna(row['中类']):
        type_name = str(row['中类']).strip()
    elif not pd.isna(row['大类']):
        type_name = str(row['大类']).strip()
    else:
        continue
    
    if type_code and type_name:
        # 转换为Java枚举格式
        # 使用类型代码作为枚举名称基础，确保符合Java命名规范
        enum_name = f"TYPE_{type_code}"
        enum_content += f"    {enum_name}(\"{type_code}\", \"{type_name}\"),\n"

# 结束枚举类
enum_content += ''';

    private final String code;
    private final String name;

    POIEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据代码获取枚举值
     */
    public static POIEnum getByCode(String code) {
        for (POIEnum poi : values()) {
            if (poi.code.equals(code)) {
                return poi;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举值
     */
    public static POIEnum getByName(String name) {
        for (POIEnum poi : values()) {
            if (poi.name.equals(name)) {
                return poi;
            }
        }
        return null;
    }
}
'''

# 输出文件路径
output_path = r'd:\fzu\Software\homework2\zeyuli\zeyuli-job\src\main\java\com\zeyuli\enm\POIEnum.java'

# 写入文件
with open(output_path, 'w', encoding='UTF-8') as f:
    f.write(enum_content)

print(f"POIEnum.java文件已生成，路径：{output_path}")
