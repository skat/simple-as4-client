package dk.toldst.eutk.declaration.marshallerMap;

import dk.toldst.eutk.declaration.obejctfactory.EutkObjectFactory;
import dk.toldst.eutk.utility.jaxb.Marshalling;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeclarationMarshallerMap {
    private Map<Class<?>, Marshalling> marshallingMap = new HashMap<>();
    private Map<Class<?>, EutkObjectFactory> objectFactoryMap = new HashMap<>();
    public <T> Marshalling getMarshaller(Class<T> clazz){
        return marshallingMap.get(clazz);
    }

    public void setMarshaller(Class<?> o, Marshalling marshalling) {
        marshallingMap.put(o, marshalling);
    }

    public Set<Class<?>> getMarshallerKeySet(){
        return marshallingMap.keySet();
    }

    public <T> EutkObjectFactory getObjectFactory(Class<T> clazz){
        return objectFactoryMap.get(clazz);
    }

    public void setObjectFactory(Class<?> o, EutkObjectFactory EutkObjectFactory) {
        objectFactoryMap.put(o, EutkObjectFactory);
    }
    
}
