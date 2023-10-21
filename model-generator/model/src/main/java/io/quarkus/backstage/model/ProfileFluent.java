package io.quarkus.backstage.model;

import java.lang.SuppressWarnings;
import io.sundr.builder.BaseFluent;
import java.lang.Object;
import java.lang.String;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Generated
 */
@SuppressWarnings("unchecked")
public class ProfileFluent<A extends ProfileFluent<A>> extends BaseFluent<A>{
  public ProfileFluent() {
  }
  
  public ProfileFluent(Profile instance) {
    this.copyInstance(instance);
  }
  private String displayName;
  private String email;
  private String picture;
  private Map<String,Object> additionalProperties = new LinkedHashMap<String,Object>();
  
  protected void copyInstance(Profile instance) {
    if (instance != null) {
        this.withDisplayName(instance.getDisplayName());
        this.withEmail(instance.getEmail());
        this.withPicture(instance.getPicture());
        this.withAdditionalProperties(instance.getAdditionalProperties());
      }
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
  
  public A withDisplayName(String displayName) {
    this.displayName=displayName; return (A) this;
  }
  
  public boolean hasDisplayName() {
    return this.displayName != null;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public A withEmail(String email) {
    this.email=email; return (A) this;
  }
  
  public boolean hasEmail() {
    return this.email != null;
  }
  
  public String getPicture() {
    return this.picture;
  }
  
  public A withPicture(String picture) {
    this.picture=picture; return (A) this;
  }
  
  public boolean hasPicture() {
    return this.picture != null;
  }
  
  public A addToAdditionalProperties(String key,Object value) {
    if(this.additionalProperties == null && key != null && value != null) { this.additionalProperties = new LinkedHashMap(); }
    if(key != null && value != null) {this.additionalProperties.put(key, value);} return (A)this;
  }
  
  public A addToAdditionalProperties(Map<String,Object> map) {
    if(this.additionalProperties == null && map != null) { this.additionalProperties = new LinkedHashMap(); }
    if(map != null) { this.additionalProperties.putAll(map);} return (A)this;
  }
  
  public A removeFromAdditionalProperties(String key) {
    if(this.additionalProperties == null) { return (A) this; }
    if(key != null && this.additionalProperties != null) {this.additionalProperties.remove(key);} return (A)this;
  }
  
  public A removeFromAdditionalProperties(Map<String,Object> map) {
    if(this.additionalProperties == null) { return (A) this; }
    if(map != null) { for(Object key : map.keySet()) {if (this.additionalProperties != null){this.additionalProperties.remove(key);}}} return (A)this;
  }
  
  public Map<String,Object> getAdditionalProperties() {
    return this.additionalProperties;
  }
  
  public <K,V>A withAdditionalProperties(Map<String,Object> additionalProperties) {
    if (additionalProperties == null) { this.additionalProperties =  null;} else {this.additionalProperties = new LinkedHashMap(additionalProperties);} return (A) this;
  }
  
  public boolean hasAdditionalProperties() {
    return this.additionalProperties != null;
  }
  
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ProfileFluent that = (ProfileFluent) o;
    if (!java.util.Objects.equals(displayName, that.displayName)) return false;
  
    if (!java.util.Objects.equals(email, that.email)) return false;
  
    if (!java.util.Objects.equals(picture, that.picture)) return false;
  
    if (!java.util.Objects.equals(additionalProperties, that.additionalProperties)) return false;
  
    return true;
  }
  
  public int hashCode() {
    return java.util.Objects.hash(displayName,  email,  picture,  additionalProperties,  super.hashCode());
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    if (displayName != null) { sb.append("displayName:"); sb.append(displayName + ","); }
    if (email != null) { sb.append("email:"); sb.append(email + ","); }
    if (picture != null) { sb.append("picture:"); sb.append(picture + ","); }
    if (additionalProperties != null && !additionalProperties.isEmpty()) { sb.append("additionalProperties:"); sb.append(additionalProperties); }
    sb.append("}");
    return sb.toString();
  }
  

}