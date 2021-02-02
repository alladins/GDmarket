# GDmarket
GDmarket : 근대마켓 - 근거리 대여 마켓


테스트 입니다.








## 무정지 재배포

* 첫째 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscale 이나 CB 설정을 제거함
* 둘째 ITEM 서비스에 readiness 옵션이 없는 배포 옵션을 가지거나, 아예 배포 설정 옵션이 없는 상황에서.
* 셋째 ITEM 서비스의 버전이 0.2 버전에서 0.3 버전의 이미지를 만들어 ACR에 PUSH함.
  - 배포작업 직전에 seige 로 워크로드를 모니터링 함.
```  
kubectl exec -it siege -- /bin/bash
siege -c100 -t120S -r10 -v --content-type "application/json" 'http://item:8080/items POST {"itemName": "Juice", "itemPrice":100}'
```
  - ITEM 서비스의 버전을 0.2 버전에서 0.3 버전 업데이트 시킴.
```  
kubectl set image deploy item item=skcc10.azurecr.io/item:0.3
```
- readiness 옵션이 없는 경우 배포 중 서비스 요청처리 실패

![image](https://user-images.githubusercontent.com/77088104/106592874-6724e080-6593-11eb-9bab-916fc39ffc3d.png)

![image](https://user-images.githubusercontent.com/77088104/106598845-a48d6c00-659b-11eb-89e9-61a2b69d1ff3.png)

- deployment.yml에 readiness 옵션을 추가한 배포 옵션을 설정
```
kubectl apply -f kubernetes/deployment.yml
```
  - 배포작업 직전에 seige 로 워크로드를 모니터링 함.
```  
kubectl exec -it siege -- /bin/bash
siege -c100 -t120S -r10 -v --content-type "application/json" 'http://item:8080/items POST {"itemName": "Juice", "itemPrice":100}'
```
  - ITEM 서비스의 버전을 0.3 버전에서 0.2 버전으로 다운그레이드 합.
```  
kubectl set image deploy item item=skcc10.azurecr.io/item:0.2
```
- readiness 옵션을 배포 옵션을 설정 한 경우 배포 중 서비스 요청처리 성공
- Availability: 100.00 % 확인

![image](https://user-images.githubusercontent.com/77088104/106608169-69913580-65a7-11eb-9514-1c8c137d29fc.png)

- 기존 버전과 새 버전의 store pod 공존 중

![image](https://user-images.githubusercontent.com/77088104/106607775-08696200-65a7-11eb-9f90-3b4aade9ee85.png)

