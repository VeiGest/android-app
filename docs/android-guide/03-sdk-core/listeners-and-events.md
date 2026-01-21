# 🔔 Listeners & Observer Pattern

Como as chamadas de rede são assíncronas, o SDK usa Interfaces para avisar o UI quando os dados estão prontos. Este é o padrão **Observer**.

## 1. Defining a Listener

Each module in the SDK has its own listener interface. For example:
```java
public interface VeiculosListener {
    void onRefreshListaVeiculos(ArrayList<Vehicle> listaVeiculos);
}
```

## 2. Registering in the Fragment

O seu Fragment deve implementar a interface e registar-se no Singleton:

```java
public class VehiclesFragment extends Fragment implements VeiculosListener {

    @Override
    public void onResume() {
        super.onResume();
        // Register the listener
        singleton.setVeiculosListener(this);
    }

    @Override
    public void onRefreshListaVeiculos(ArrayList<Vehicle> lista) {
        // Update your adapter here
        adapter.setVehicles(lista);
    }
}
```

## 3. Triggering the Update from SDK

Dentro do `SingletonVeiGest`, após o parsing do JSON terminar:

```java
if (veiculosListener != null) {
    veiculosListener.onRefreshListaVeiculos(veiculos);
}
```

## 4. Important: Managing Registration

Para evitar **memory leaks**, certifique-se de que o listener é removido quando o fragmento não está visível:

```java
@Override
public void onPause() {
    super.onPause();
    // Remove the listener to avoid keeping a reference to a closed screen
    singleton.setVeiculosListener(null);
}
```

## 5. Multiple Listeners?
Atualmente, o SDK suporta um listener por tipo de dado. Se precisar de múltiplos ouvintes em simultâneo (ex: Dashboard e Vehicles List abertos ao mesmo tempo), seria necessário evoluir para uma `List<VeiculosListener>`.
