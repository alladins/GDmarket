# GDmarket
GDmarket : 근대마켓 - 근거리 대여 마켓


테스트 입니다.

![image](https://user-images.githubusercontent.com/77088104/106592874-6724e080-6593-11eb-9bab-916fc39ffc3d.png)
![image](https://user-images.githubusercontent.com/73699193/98105334-2a394700-1edb-11eb-9633-f5c33c5dee9f.png)

![image](https://user-images.githubusercontent.com/77088104/106598845-a48d6c00-659b-11eb-89e9-61a2b69d1ff3.png)




## 무정지 재배포

* 먼저 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscale 이나 CB 설정을 제거함


- seige 로 배포작업 직전에 워크로드를 모니터링 함.
```
kubectl apply -f kubernetes/deployment_readiness.yml
```
- readiness 옵션이 없는 경우 배포 중 서비스 요청처리 실패

![image](https://user-images.githubusercontent.com/73699193/98105334-2a394700-1edb-11eb-9633-f5c33c5dee9f.png)


- deployment.yml에 readiness 옵션을 추가

![image](https://user-images.githubusercontent.com/73699193/98107176-75ecf000-1edd-11eb-88df-617c870b49fb.png)

- readiness적용된 deployment.yml 적용

```
kubectl apply -f kubernetes/deployment.yml
```
- 새로운 버전의 이미지로 교체
```
cd acr
az acr build --registry admin02 --image admin02.azurecr.io/store:v4 .
kubectl set image deploy store store=admin02.azurecr.io/store:v4 -n phone82
```
- 기존 버전과 새 버전의 store pod 공존 중

![image](https://user-images.githubusercontent.com/73699193/98106161-65884580-1edc-11eb-9540-17a3c9bdebf3.png)

- Availability: 100.00 % 확인

![image](https://user-images.githubusercontent.com/73699193/98106524-c152ce80-1edc-11eb-8e0f-3731ca2f709d.png)

