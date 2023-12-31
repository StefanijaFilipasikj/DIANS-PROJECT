from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import StaleElementReferenceException
from time import sleep
import pandas as pd

if __name__ == '__main__':
    options = webdriver.ChromeOptions()
    options.add_argument('--headless')
    options.add_argument('--no-sandbox')
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument('--disable-blink-features=AutomationControlled')

    browser = webdriver.Chrome(options=options)

    data = pd.read_csv('data.csv')

    print("Loading page")
    browser.get('https://www.itilog.com/')
    addresses = []

    for i in range(len(data)):
        print(f"Getting {i} data")
        lat = (data['lat'][i]).astype(str)
        lon = (data['lon'][i]).astype(str)

        lat_input = browser.find_element(By.CSS_SELECTOR, 'input#latitude')
        lon_input = browser.find_element(By.CSS_SELECTOR, 'input#longitude')

        lat_input.clear()
        lon_input.clear()

        lat_input.send_keys(lat)
        lon_input.send_keys(lon)

        browser.find_element(By.CSS_SELECTOR, 'input#coordinates_to_map').click()

        sleep(5)

        WebDriverWait(browser, 10).until(EC.presence_of_element_located((By.CSS_SELECTOR, 'div#info_window')))
        element = browser.find_element(By.CSS_SELECTOR, 'div#info_window')
        addresses.append(element.text.split('\n')[0])
    print("Done getting data")

    regions = []
    for i in range(len(addresses)):
        parts = addresses[i].split(', ')
        region = parts[-2]

        r_parts = region.split(' ')

        if r_parts[0].isdigit():
            c = ' '.join(r_parts[1:])
            regions.append(c)
        else:
            c = ' '.join(r_parts[0:])
            regions.append(c)

    data['Address'] = [element.replace(',', ' ') for element in addresses]
    data['Region'] = regions

    data.columns = ['Id', 'Lat', 'Lon', 'Historic', 'Name', 'Address', 'Region']

    data.to_csv('updated_data.csv', index=False)
    print("Saved to csv")
