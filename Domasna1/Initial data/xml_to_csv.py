import xml.etree.ElementTree as ET
import csv


def xml_to_json(tree):
    root = tree.getroot()

    filtered_data = []
    for node in root.findall('node'):
        tags = node.findall('tag')
        historic_tags = [tag for tag in tags if tag.get('k') == 'historic']
        name_tag = next((tag for tag in tags if tag.get('k') == 'name'), None)

        if historic_tags:
            node_data = {
                'id': node.get('id'),
                'lat': node.get('lat'),
                'lon': node.get('lon'),
            }
            for tag in historic_tags:
                node_data[tag.get('k')] = tag.get('v')
            if name_tag is not None:
                node_data['name'] = name_tag.get('v')
            filtered_data.append(node_data)

    filtered_data_with_name = [node_data for node_data in filtered_data if 'name' in node_data]
    filtered_data_without_boundary_stone = [node_data for node_data in filtered_data_with_name if
                                            node_data.get('historic') != 'boundary_stone']
    filtered_data_without_aircraft = [node_data for node_data in filtered_data_without_boundary_stone if
                                      node_data.get('historic') != 'aircraft']
    filtered_data_without_locomotive = [node_data for node_data in filtered_data_without_aircraft if
                                        node_data.get('historic') != 'locomotive']
    not_valid_ids = ['1452854994', '2138788013', '5046404750', '5541143272', '5551392390', '5551392391', '5551392392',
                     '5551410938', '5551410940', '5551410941', '6821158694', '7473901313', '7473959214', '8534237588',
                     '8901338318', '8901338319', '8901349094', '10006874002', '10204019571', '10204019572',
                     '8402562316',
                     '8402601134', '9447094184', '9447094185', '9447094186', '9447094187', '9927093810', '9927105104',
                     '9927105105']
    final_data = [node_data for node_data in filtered_data_without_locomotive if
                  node_data.get('id') not in not_valid_ids]

    return final_data


def save_to_csv(final_data):
    csv_file_path = 'data.csv'

    csv_header = final_data[0].keys()

    with open(csv_file_path, 'w', newline='', encoding='utf-8') as csv_file:
        csv_writer = csv.DictWriter(csv_file, fieldnames=csv_header)
        csv_writer.writeheader()
        csv_writer.writerows(final_data)
    print('Document saved as csv!!')


def main():
    tree = ET.parse("macedonia-latest.osm")
    filtered_data = xml_to_json(tree)
    save_to_csv(filtered_data)
    # print(filtered_data)


if __name__ == "__main__":
    main()
