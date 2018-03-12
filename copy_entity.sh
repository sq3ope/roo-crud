#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <src_entity> <src_entity_plural> <dest_entity> <dest_entity_plural>"
    echo "eg. $0 person people pet pets"
    exit 1
fi

src_entity=$1
src_entity_plural=$2
dest_entity=$3
dest_entity_plural=$4
src_entity_upper=${src_entity^}
dest_entity_upper=${dest_entity^}
src_entity_plural_upper=${src_entity_plural^}
dest_entity_plural_upper=${dest_entity_plural^}

replace_commands="s/$src_entity_plural_upper/$dest_entity_plural_upper/g; s/$src_entity_plural/$dest_entity_plural/g; s/$src_entity_upper/$dest_entity_upper/g; s/$src_entity/$dest_entity/g"

sed "$replace_commands" "src/main/java/crud/roo/domain/${src_entity_upper}.java" > "src/main/java/crud/roo/domain/${dest_entity_upper}.java"
sed "$replace_commands" "src/main/java/crud/roo/web/${src_entity_upper}Controller.java" > "src/main/java/crud/roo/web/${dest_entity_upper}Controller.java"

src_view_dir="src/main/webapp/WEB-INF/views/${src_entity_plural}"
dest_view_dir="src/main/webapp/WEB-INF/views/${dest_entity_plural}"

mkdir -p "${dest_view_dir}"

sed "$replace_commands" "${src_view_dir}/create.jspx" > "${dest_view_dir}/create.jspx"
sed "$replace_commands" "${src_view_dir}/list.jspx" > "${dest_view_dir}/list.jspx"
sed "$replace_commands" "${src_view_dir}/show.jspx" > "${dest_view_dir}/show.jspx"
sed "$replace_commands" "${src_view_dir}/update.jspx" > "${dest_view_dir}/update.jspx"
sed "$replace_commands" "${src_view_dir}/views.xml" > "${dest_view_dir}/views.xml"

app_properties_file='src/main/webapp/WEB-INF/i18n/application.properties'
cat "${app_properties_file}" | grep "^label_crud_roo_domain_${src_entity}[=_]\|menu_category_${src_entity}_label\|menu_item_${src_entity}_" | sed "$replace_commands" >> "${app_properties_file}"
cat "${app_properties_file}" | sort | uniq > "${app_properties_file}.tmp"
mv "${app_properties_file}.tmp" "${app_properties_file}"
