# K8s-Native Emulation Component

This repository contains the K8s-native Emulation Component (EmuComp). It is based on code
from the [Emulation-as-a-Service](https://gitlab.com/emulation-as-a-service/eaas-server)
project, which has been heavily modified to make it natively runnable in Kubernetes.

## License

This project is licensed under the GPL-3.0 license. See the [LICENSE](./LICENSE) file and
the original [Emulation-as-a-Service](https://gitlab.com/emulation-as-a-service/eaas-server)
repository for more details.

Scaling
kubectl scale statefulset emucomp --replicas=<N>
emucomp-<N>.emucomp-headless.default.svc.cluster.local